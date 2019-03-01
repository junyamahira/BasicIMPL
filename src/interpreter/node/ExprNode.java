package interpreter.node;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.LexicalUnit;
import interpreter.Value;
import interpreter.ValueImpl;
import interpreter.ValueType;

public class ExprNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.SUB,
					LexicalType.LP, LexicalType.NAME, LexicalType.INTVAL,
					LexicalType.DOUBLEVAL, LexicalType.LITERAL));
	private Node leftNode;
    private Node rightNode;
    private LexicalType operand;
    private boolean isMono = false; //単項式用に追加
    private final static Map<LexicalType, Integer> OPERATORS = new HashMap<>();
    static{
        OPERATORS.put(LexicalType.DIV,2);
        OPERATORS.put(LexicalType.MUL,2);
        OPERATORS.put(LexicalType.SUB,3);
        OPERATORS.put(LexicalType.ADD,4);
    }

    private ExprNode(Environment env) {
        super(env);
        type = NodeType.EXPR;
    }

    //単項式用
    private ExprNode(Environment env, boolean b) {
        super(env);
        type = NodeType.EXPR;
        isMono = b;
    }

    public ExprNode(Node l, Node r, LexicalType o) {
        type = NodeType.EXPR;
        leftNode = l;
        rightNode = r;
        operand = o;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new ExprNode(env);
    }

    //???
    @Override
    public boolean parse() throws Exception {
        Deque<Node> exprStack = new ArrayDeque<>();
        Deque<LexicalUnit> operStack = new ArrayDeque<>();

        while (true) {
            switch (env.getInput().peep().getType()) {
	            case LP:
	                env.getInput().get();
	                Node bracket = ExprNode.getHandler(env);
	                bracket.parse();
	                exprStack.add(bracket);
	                if (env.getInput().expect(LexicalType.RP)) {
	                    env.getInput().get();
	                } else {
	                    throw new Exception("Expr:閉じる括弧が不足");
	                }
	                break;
	            case INTVAL:
                case DOUBLEVAL:
                case LITERAL:
                    exprStack.add(ConstNode.getHandler(env.getInput().get().getValue()));
                    break;
                case SUB:
                    env.getInput().get();
                    if (!ExprNode.isMatch(env.getInput().peep().getType())) {
                        throw new Exception("Expr:-の後に続く式が見当たりません");
                    }
                    Node subNode = new ExprNode(env, true);
                    subNode.parse();
                    exprStack.add(subNode);
                    break;
                case NAME:
                    if (env.getInput().peep(2).getType() == LexicalType.LP) {
                        Node funcNode = FunctionCallNode.getHandler(env);
                        funcNode.parse();
                        exprStack.add(funcNode);

                    } else {
                        exprStack.add(env.getVariable(env.getInput().get().getValue().getSValue()));
                    }
                    break;
                default:
                    throw new Exception("Expr:式の項として不適切な型 = " + env.getInput().peep().getType());
            }

            //単項式のとき
            if (isMono) {
                leftNode = ConstNode.getHandler(new ValueImpl(-1));
                operand = LexicalType.MUL;
                rightNode = exprStack.pollLast();
                return true;
            }

            if (OPERATORS.containsKey(env.getInput().peep().getType())) {
                if (operStack.size() > 0 &&
                		OPERATORS.get(env.getInput().peep().getType()) >= OPERATORS.get(operStack.peekLast().getType())) {
                    Node rightNode = exprStack.pollLast();
                    Node leftNode = exprStack.pollLast();
                    LexicalType operand = operStack.pollLast().getType();
                    exprStack.add(new ExprNode(leftNode, rightNode, operand));
                    operStack.add(env.getInput().get());

                } else {
                    operStack.add(env.getInput().get());
                }
            } else {
                break;
            }
        }

        int nOper = operStack.size();

        if ((operStack.size() > 0) && OPERATORS.get(operStack.peekLast().getType()) == 2) {
            Node r = exprStack.pollLast();
            Node l = exprStack.pollLast();
            LexicalType op = operStack.pollLast().getType();
            exprStack.addLast(new ExprNode(l, r, op));
        }

        while (!operStack.isEmpty()) {

            Node l = exprStack.pollFirst();
            Node r = exprStack.pollFirst();
            LexicalType op = operStack.pollFirst().getType();
            exprStack.addFirst(new ExprNode(l, r, op));
        }
        leftNode = exprStack.pollLast();

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        //単項式であるかどうかで分岐
        if (operand == null) {
            return leftNode.getValue();
        } else {
            Value val1 = leftNode.getValue();
            Value val2 = rightNode.getValue();

            if (val1.getType() == ValueType.INTEGER && val2.getType() == ValueType.INTEGER) {
                if (operand == LexicalType.ADD) {
                    return new ValueImpl(val1.getIValue() + val2.getIValue());
                } else if (operand == LexicalType.SUB) {
                    return new ValueImpl(val1.getIValue() - val2.getIValue());
                } else if (operand == LexicalType.MUL) {
                    return new ValueImpl(val1.getIValue() * val2.getIValue());
                } else if (operand == LexicalType.DIV) {
                    return new ValueImpl(val1.getIValue() / val2.getIValue());
                } else {
                    throw new Exception("Expr:不正な演算子で演算");
                }
            } else if ((val1.getType() == ValueType.INTEGER || val1.getType() == ValueType.DOUBLE)
                    && (val2.getType() == ValueType.INTEGER || val2.getType() == ValueType.DOUBLE)) {
                if (operand == LexicalType.ADD) {
                    return new ValueImpl(val1.getDValue() + val2.getDValue());
                } else if (operand == LexicalType.SUB) {
                    return new ValueImpl(val1.getDValue() - val2.getDValue());
                } else if (operand == LexicalType.MUL) {
                    return new ValueImpl(val1.getDValue() * val2.getDValue());
                } else if (operand == LexicalType.DIV) {
                    return new ValueImpl(val1.getDValue() / val2.getDValue());
                } else {
                    throw new Exception("Expr:不正な演算子で演算");
                }
            } else if (val1.getType() == ValueType.STRING || val2.getType() == ValueType.STRING) {
                if (operand == LexicalType.ADD) {
                    return new ValueImpl(val1.getSValue() + val2.getSValue());
                }else{
                    throw new Exception("Expr:不正な演算子で演算(文字列)");
                }
            } else {
                throw new Exception("Expr:実行できない演算");
            }
        }
    }

    @Override
    public String toString() {
        if (operand == null) {
            return "" + leftNode;
        } else if (isMono) {
            return "-" + rightNode;
        } else {
            return String.format("(%s,%s,%s)", leftNode, rightNode, OPERATORS.get(operand));
        }
    }

}

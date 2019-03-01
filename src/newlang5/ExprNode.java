package newlang5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



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
        OPERATORS.put(LexicalType.DIV,1);
        OPERATORS.put(LexicalType.MUL,2);
        OPERATORS.put(LexicalType.SUB,3);
        OPERATORS.put(LexicalType.ADD,4);
    }

    public ExprNode(Environment env) {
    	super(env);
    	type = NodeType.EXPR;
	}

    public ExprNode(Node leftNode, Node rightNode, LexicalType op) {
    	this.leftNode = leftNode;
    	this.rightNode = rightNode;
    	operand = op;
    	type = NodeType.EXPR;
	}

    //単項式用に追加
    private ExprNode(Environment env, boolean b) {
        super(env);
        type = NodeType.EXPR;
        isMono = b;
    }

    public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

    public static Node getHandler(Environment env) {
    	return new ExprNode(env);
	}

    @Override
    public boolean Parse() throws Exception {
    	List<Node> result=new ArrayList<>();
        List<LexicalType> operators = new ArrayList<>();

        while (true) {
			switch (env.getInput().peep(1).getType()) {
            case INTVAL:
            case DOUBLEVAL:
            case LITERAL:
                result.add(ConstNode.getHandler(env.getInput().get().getValue()));
                break;

            case LP:
                env.getInput().get();
                Node handler = ExprNode.getHandler(env);
                handler.Parse();
                result.add(handler);
                if (!env.getInput().expect(LexicalType.RP)) {
                    throw new Exception("Expr:式の括弧が正しくありません");
                }
                break;

            case SUB:
                env.getInput().get();
                if (!ExprNode.isMatch(env.getInput().peep(1).getType())) {
                    throw new Exception("Expr:-の後ろに式が見当たりません");
                }
                Node subNode = new ExprNode(env, true);
                subNode.Parse();
                result.add(subNode);
                break;
            case NAME:
                if (env.getInput().peep(2).getType() == LexicalType.LP) {   //関数呼び出しのとき
                    Node funcNode =CallSubFuncNode.getHandler(env);
                    funcNode.Parse();
                    result.add(funcNode);
                } else {
                    result.add(new VariableNode(env.getInput().get().getValue().getSValue()));   //ただの変数
                }
                break;
            default:
                throw new Exception("Expr:式の項として不正です[" + env.getInput().peep(1).getType() + "]");
			}

            //単項式
            if (isMono) {
                leftNode = ConstNode.getHandler(new ValueImpl("-1", ValueType.INTEGER));
                operand = LexicalType.MUL;
                rightNode = result.remove(result.size()-1);
                return true;
            }

            if (OPERATORS.containsKey(env.getInput().peep(1).getType())) {
                if (operators.size() > 0 &&
                		OPERATORS.get(env.getInput().peep(1).getType()) <= OPERATORS.get(operators.remove(operators.size()-1))) {
                    Node right = result.remove(result.size()-1);
                    Node left = result.remove(result.size()-1);
                    LexicalType operand = operators.remove(operators.size()-1);
                    result.add(new ExprNode(left, right, operand));
                    operators.add(env.getInput().get().getType());

                } else operators.add(env.getInput().get().getType());

            } else {
                break;
            }
        }

        int nOperators = operators.size();
        for (int i = 0; i < nOperators; i++) {
            Node l = result.remove(0);
            Node r = result.remove(0);
            LexicalType op = operators.remove(0);
            result.add(0, new ExprNode(l, r, op));
        }
        leftNode = result.remove(result.size()-1);

        while (!operStack.isEmpty()) {

            Node l = exprStack.pollFirst();
            Node r = exprStack.pollFirst();
            LexicalType op = operStack.pollFirst().getType();
            exprStack.addFirst(new ExprNode(l, r, op));
        }
        left = exprStack.pollLast();

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        //単項式であるかどうかで分岐
        if (operator == null) {
            return left.getValue();
        } else {
            Value val1 = left.getValue();
            Value val2 = right.getValue();

            //数値比較 両方整数ver
            if (val1.getType() == ValueType.INTEGER && val2.getType() == ValueType.INTEGER) {
                if (operator == LexicalType.ADD) {
                    return new ValueImpl(val1.getIValue() + val2.getIValue());
                } else if (operator == LexicalType.SUB) {
                    return new ValueImpl(val1.getIValue() - val2.getIValue());
                } else if (operator == LexicalType.MUL) {
                    return new ValueImpl(val1.getIValue() * val2.getIValue());
                } else if (operator == LexicalType.DIV) {
                    return new ValueImpl(val1.getIValue() / val2.getIValue());
                } else if (operator == LexicalType.MOD) {
                    return new ValueImpl(val1.getIValue() % val2.getIValue());
                } else {
                    throw new Exception("不正な演算子で演算を試みました。");
                }
            } else if ((val1.getType() == ValueType.INTEGER || val1.getType() == ValueType.DOUBLE)
                    && (val2.getType() == ValueType.INTEGER || val2.getType() == ValueType.DOUBLE)) {
                //どちらかが実数ver 実数にキャストする
                if (operator == LexicalType.ADD) {
                    return new ValueImpl(val1.getDValue() + val2.getDValue());
                } else if (operator == LexicalType.SUB) {
                    return new ValueImpl(val1.getDValue() - val2.getDValue());
                } else if (operator == LexicalType.MUL) {
                    return new ValueImpl(val1.getDValue() * val2.getDValue());
                } else if (operator == LexicalType.DIV) {
                    return new ValueImpl(val1.getDValue() / val2.getDValue());
                } else if (operator == LexicalType.MOD) {
                    return new ValueImpl(val1.getDValue() % val2.getDValue());
                } else {
                    throw new Exception("不正な演算子で演算を試みました。");
                }
            } else if (val1.getType() == ValueType.STRING || val2.getType() == ValueType.STRING) {
                if (operator == LexicalType.ADD) {
                    //文字列の足し算に変更
                    return new ValueImpl(val1.getSValue() + val2.getSValue());
                }else{
                    throw new Exception("文字列の計算は加算のみ行えます");
                }
            } else {
                throw new Exception("実行できない演算を試みました");
            }
        }
    }

    public String toString() {
        if (operator == null) {
            return "" + left;
        } else if (isMono) {
            return "-" + right;
        } else {
            return String.format("(%s,%s,%s)", left, right, OPER_SET.get(operator));
        }
    }

    //結合の優先度を返す
    private int getOpePriority(LexicalType type) {
        switch (type) {
            case MUL:
            case DIV:
            case MOD:
                return 2;
            case SUB:
            case ADD:
                return 1;
            default:
                return -1;
        }
    }
}

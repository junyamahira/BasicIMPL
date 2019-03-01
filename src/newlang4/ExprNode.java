package newlang4;

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

        return true;
    }

    @Override
    public String toString() {
        if (operand == null) {
            return "" + leftNode;
        } else if (isMono) {
            return "-" + rightNode;
        } else {
            return String.format("(%s)(%s)%s", leftNode, rightNode, OPERATORS.get(operand));
        }
    }

    @Override
    public Value getValue() throws Exception {
        //単項式であるかどうかで分岐
        if (operand == null) {
            return leftNode.getValue();
        } else {
            Value val1 = leftNode.getValue();
            Value val2 = rightNode.getValue();

            //数値比較 両方整数ver
            if (val1.getType() == ValueType.INTEGER && val2.getType() == ValueType.INTEGER) {
            	int v1 = val1.getIValue();
            	int v2 = val2.getIValue();
            	String src;

                if (operand == LexicalType.ADD) {
                	src = Integer.toString(v1+v2);
                } else if (operand == LexicalType.SUB) {
                	src = Integer.toString(v1-v2);
                } else if (operand == LexicalType.MUL) {
                	src = Integer.toString(v1*v2);
                } else if (operand == LexicalType.DIV) {
                	src = Integer.toString(v1/v2);
                } else {
                    throw new Exception("Expr:不正な演算子、演算できません");
                }
                return new ValueImpl(src, ValueType.INTEGER);

            } else if ((val1.getType() == ValueType.INTEGER || val1.getType() == ValueType.DOUBLE) &&
            		(val2.getType() == ValueType.INTEGER || val2.getType() == ValueType.DOUBLE)) {
            	double v1 = val1.getDValue();
            	double v2 = val2.getDValue();
            	String src;

                if (operand == LexicalType.ADD) {
                    src = Double.toString(v1+v2);
                } else if (operand == LexicalType.SUB) {
                	src = Double.toString(v1-v2);
                } else if (operand == LexicalType.MUL) {
                	src = Double.toString(v1*v2);
                } else if (operand == LexicalType.DIV) {
                	src = Double.toString(v1/v2);
                } else {
                    throw new Exception("Expr:不正な演算子、演算できません");
                }
                return new ValueImpl(src, ValueType.DOUBLE);

            } else if (val1.getType() == ValueType.STRING || val2.getType() == ValueType.STRING) {
            	String src = val1.getSValue() + val2.getSValue();
                if (operand == LexicalType.ADD) {
                    return new ValueImpl(src, ValueType.STRING);
                }else throw new Exception("Expr:Stringは加算しかできません");

            } else throw new Exception("Expr:演算不可");
        }
    }

}

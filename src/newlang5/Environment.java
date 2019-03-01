package newlang5;

import java.util.HashMap;

import funcs.Function;
import funcs.PrintFunction;
import newlang5.node.VariableNode;

public class Environment {

    LexicalAnalyzer input;
    HashMap<String, VariableNode> var_table;
    HashMap<String, Function> library;

    public Environment(LexicalAnalyzer my_input) {
        input = my_input;
        var_table = new HashMap<>();
        library = new HashMap<>();
        library.put("PRINT", new PrintFunction());
    }

    public LexicalAnalyzer getInput() {
        return input;
    }

    public Function getFunction(String fname) {
        return (Function) library.get(fname);
    }

    public VariableNode getVariable(String vname) {
        VariableNode v;
        v = (VariableNode) var_table.get(vname);
        if (v == null) {
            v = new VariableNode(vname);
            var_table.put(vname, v);
        }
        return v;
    }
}

/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
 *
 */
public class FunctionCall implements Expression {

	/**
	 * Name of the called function.
	 * TODO : Should be an expression.
	 */
	protected String name;
	
	/**
	 * Declaration of the called function after name resolution.
	 * TODO : Should rely on the VariableUse class.
	 */
	protected FunctionDeclaration function;
	
	/**
	 * List of AST nodes that computes the values of the parameters for the function call.
	 */
	protected List<Expression> arguments;
	
	/**
	 * @param _name : Name of the called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public FunctionCall(String _name, List<Expression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.function == null)?this.name:this.function) + "( ";
		Iterator<Expression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		SymbolTable _local = new SymbolTable(_scope);
		if(checkFunctionDefinition(_local)){
			boolean result = true;
			for(Expression exp : arguments){
				result = result && exp.collect(_local);
			}
			return result && function.collect(_local);
		}
		else
			throw new SemanticsUndefinedException("Error : the function " + this.name +" doesnt exist");

	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		SymbolTable _local = new SymbolTable(_scope);
		if(checkFunctionDefinition(_local)){
			boolean result = true;
			for(Expression exp : arguments){
				result = result && exp.resolve(_local);
			}
			return result && function.resolve(_local);
		}
		else
			throw new SemanticsUndefinedException("Error : the function " + this.name + " doesnt exist");

	}

	/**
	 * @param _local Inherited Scope that should contain the declaration of the called the function
	 * @return indicates if the function is declared or not
	 */
	private boolean checkFunctionDefinition(SymbolTable _local){
		// get the name of the function from the AST nodes
		//and check if the function is already defined
		Declaration functionName = _local.get(this.name);
		if(functionName instanceof FunctionCall){
			this.function = (FunctionDeclaration) functionName;
			return true;
		}
		return false;

	}
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return function.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		throw new SemanticsUndefinedException( "Semantics getCode is undefined in FunctionCall.");
	}

}

/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.accessible.AddressAccess;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
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
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		Declaration declaration = _scope.get(this.name);
		boolean result = true;
		if(declaration instanceof FunctionDeclaration) {
			this.function = (FunctionDeclaration) declaration;
			for (Expression e : this.arguments) {
				result = e.collectAndPartialResolve(_scope) && result;
			}

			return result && arguments.size() == function.getParameters().size();
		}
		else
			throw new SemanticsUndefinedException("Error : the function " + this.name + " doesnt exist");

	}


	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		Declaration declaration = _scope.get(this.name);
		boolean result = true;
		if(declaration instanceof FunctionDeclaration) {
			this.function = (FunctionDeclaration) declaration;
			for (Expression e : this.arguments) {
				result = e.completeResolve(_scope) && result;
			}
			for(int i=0;i<arguments.size();i++){
				result &= arguments.get(i).getType().compatibleWith(function.getParameters().get(i).getType());
				if(!result)
					throw new SemanticsUndefinedException("arguments types missmatched with parameters type. Expected " + function.getParameters().get(i).getType() + " , got " + arguments.get(i).getType() );
			}
			return result;
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
		if(functionName instanceof FunctionDeclaration){
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
		Fragment _frag = _factory.createFragment();
		for(int i = 0;i<arguments.size();i++){
			if(arguments.get(i).getType() instanceof PointerType){
				VariableDeclaration _varDecl;
				if(arguments.get(i) instanceof IdentifierAccess) {
					IdentifierAccess _idAccess = (IdentifierAccess) arguments.get(i);
					 _varDecl = (VariableDeclaration) _idAccess.getExpression().getDeclaration();
				}
				else{
					//Address Access in this case
					AddressAccess _adrAccess = (AddressAccess) arguments.get(i);
					_varDecl = (VariableDeclaration) ((VariableAssignment) _adrAccess.getAssignable()).getDeclaration();
				}
				System.out.println("Register : " + _varDecl.getRegister() + " : offset " + _varDecl.getOffset());
				function.getParameters().get(i).setRegister(_varDecl.getRegister());
				function.getParameters().get(i).setPointerOffset(_varDecl.getOffset());
			}
		}
		if(arguments!=null){
			for(Expression _param : arguments)
			{
				_frag.append(_param.getCode(_factory));
			}
		}

		_frag.add(_factory.createCall("function_"+this.function.getName(), this.function.getRegister()));



	//	_frag.append(this.function.getCode(_factory));


		return _frag;
	}

}

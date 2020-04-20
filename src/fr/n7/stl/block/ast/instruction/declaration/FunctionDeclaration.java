/**
 * 
 */
package fr.n7.stl.block.ast.instruction.declaration;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.Return;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.tam.ast.TAMInstruction;
import fr.n7.stl.tam.ast.impl.TAMFactoryImpl;
import fr.n7.stl.util.Logger;

import javax.print.DocFlavor;

/**
 * Abstract Syntax Tree node for a function declaration.
 * @author Marc Pantel
 */
public class FunctionDeclaration implements Instruction, Declaration {

	/**
	 * Name of the function
	 */
	protected String name;
	
	/**
	 * AST node for the returned type of the function
	 */
	protected Type type;
	
	/**
	 * List of AST nodes for the formal parameters of the function
	 */
	protected List<ParameterDeclaration> parameters;
	
	/**
	 * @return the parameters
	 */
	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}

	/**
	 * AST node for the body of the function
	 */
	protected Block body;
	/**
	 *
	 */
	protected Register register;

	/**
	 *
	 */
	protected int offset;

	/**
	 * Builds an AST node for a function declaration
	 * @param _name : Name of the function
	 * @param _type : AST node for the returned type of the function
	 * @param _parameters : List of AST nodes for the formal parameters of the function
	 * @param _body : AST node for the body of the function
	 */
	public FunctionDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		this.name = _name;
		this.type = _type;
		this.parameters = _parameters;
		this.body = _body;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = this.type + " " + this.name + "( ";
		Iterator<ParameterDeclaration> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " )" + this.body;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this.type;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if(!_scope.accepts(this)) {
			Logger.error("Error : Function Declaration resolve failed");
			return false;
		}
		else
			_scope.register(this);
		return body.collectAndPartialResolve(new SymbolTable(_scope));
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		SymbolTable _local = new SymbolTable(_scope);
		if(!_scope.accepts(this))
			Logger.error("Error : Function Declaration resolve failed");
		else{
			_scope.register(this);
			boolean _result = true;
			for(ParameterDeclaration param : parameters){
				_result = _result && param.type.completeResolve(_local);
				if(_local.accepts(param))
					_local.register(param);
				else
					return false;
			}
			return _result && body.completeResolve(_local) && type.completeResolve(_local);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		return body.checkType();
	}

	public int getOffset() {
		return offset;
	}

	/* (non-Javadoc)
         * @see fr.n7.stl.block.ast.instruction.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
         */
	@Override
//	public int allocateMemory(Register _register, int _offset) {
//		/*this.register = _register;
//		this.offset = _offset;
//		int address = 0;
//		System.out.println("FunctionDeclaration:allocateMemory");
//		// Allocate the memory using stacks
//		Collections.reverse(parameters);
//		for(ParameterDeclaration param : parameters){
//			//TODO allocate memory for the functions params
//		}
//		return address;*/
//		this.register = _register;
//		this.offset = _offset;
//		this.body.allocateMemory(Register.LB, 3);
//		return 0;
//	}
	public int allocateMemory(Register _register, int _offset) {

		this.register = _register;
		System.out.println(this.register);

		//System.exit(0);

		this.offset = _offset;
		int _paramSize = 0;
		// init parameters offset
		for(int i = parameters.size()-1;i>=0;i--){
			if(!(parameters.get(i).getType() instanceof PointerType)) {
				parameters.get(i).setRegister(this.register);
				parameters.get(i).setOffset(-1 * _paramSize);
				_paramSize += parameters.get(i).getType().length();
			}
		}
		body.allocateMemory(Register.LB,3);

		return 0;
	}
	public Register getRegister() {
		return register;
	}

	/* (non-Javadoc)
         * @see fr.n7.stl.block.ast.instruction.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
         */
	@Override
	public Fragment getCode(TAMFactory _factory) {

		Fragment _frag = _factory.createFragment();

		int id = _factory.createLabelNumber();

		int paramSize = 1;

		for(ParameterDeclaration param : parameters)

			paramSize += param.getType().length();

		_frag.append(body.getCode(_factory));

		if(!type.equalsTo(AtomicType.VoidType)) {

			_frag.add(_factory.createReturn(0, paramSize));

		}
		_frag.addPrefix("function_" + name);

		return _frag;
	}

}

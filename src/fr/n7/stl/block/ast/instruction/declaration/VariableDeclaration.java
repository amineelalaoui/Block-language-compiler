/**
 *
 */
package fr.n7.stl.block.ast.instruction.declaration;
import  java.util.*;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.call.AttributAccess;
import fr.n7.stl.poo.call.ConstructorCall;
import fr.n7.stl.poo.call.MethodCall;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.declaration.InterfaceDeclaration;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a variable declaration instruction.
 * @author Marc Pantel
 *
 */
public class VariableDeclaration implements Declaration, Instruction {

	/**
	 * Name of the declared variable.
	 */
	protected String name;

	/**
	 * AST node for the type of the declared variable.
	 */
	protected Type type;

	/**
	 * AST node for the initial value of the declared variable.
	 */
	protected Expression value;

	public Expression getValue() {
		return value;
	}

	/**
	 * Address register that contains the base address used to store the declared variable.
	 */
	protected Register register;

	/**
	 * Offset from the base address used to store the declared variable
	 * i.e. the size of the memory allocated to the previous declared variables
	 */
	protected int offset;

	/**
	 * Creates a variable declaration instruction node for the Abstract Syntax Tree.
	 * @param _name Name of the declared variable.
	 * @param _type AST node for the type of the declared variable.
	 * @param _value AST node for the initial value of the declared variable.
	 */
	public VariableDeclaration(String _name, Type _type, Expression _value) {
		this.name = _name;
		this.type = _type;
		this.value = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.type + " " + this.name + " = " + this.value + ";\n";
	}

	/**
	 * Synthesized semantics attribute for the type of the declared variable.
	 * @return Type of the declared variable.
	 */
	public Type getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.VariableDeclaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Synthesized semantics attribute for the register used to compute the address of the variable.
	 * @return Register used to compute the address where the declared variable will be stored.
	 */
	public Register getRegister() {
		return this.register;
	}

	/**
	 * Synthesized semantics attribute for the offset used to compute the address of the variable.
	 * @return Offset used to compute the address where the declared variable will be stored.
	 */
	public int getOffset() {
		return this.offset;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if(!_scope.accepts(this)) {
			Logger.error("Error : previous declaration of " + name + " already exists");
			return false;
		}
		else{
			_scope.register(this);
			return value.collectAndPartialResolve(_scope);
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if(_scope.accepts(this)){
			_scope.register(this);
			return this.value.completeResolve(_scope) && this.type.completeResolve(_scope);
		}
		else
			Logger.error("Error : previous declaration of " + name + " already exists");
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
//        System.out.println("checktype of " + type + " and " + value.getType() + " : " + type.compatibleWith(value.getType()));
		if(this.value instanceof ConstructorCall) {
			ConstructorCall construct = (ConstructorCall) this.value;
			Instanciation instanciation = (Instanciation) this.type;
			boolean resultat = construct.getInstanciation().getType().compatibleWith(instanciation.getType());
			ClasseDeclaration cd = (ClasseDeclaration) construct.getInstanciation().getDeclaration().getContainer();

			if(instanciation.getDeclaration().getContainer() instanceof InterfaceDeclaration) {
				List<Instanciation> implement = cd.getImplementations();
				for(Instanciation ins : implement) {
					if(ins.getName().equals(instanciation.getName())) {
						return true;
					}
				}
				return false;
			}else if(instanciation.getDeclaration().getContainer() instanceof ClasseDeclaration) {
				ClasseDeclaration cd1 = (ClasseDeclaration) instanciation.getDeclaration().getContainer();
				if(cd.getExtension().getInstanciation() != null){
					if(cd.getExtension().getInstanciation().getName().equals(cd1.getName())) {
						return true;
					}
					return false;

				}
			}
			return resultat;

		}else if(this.value instanceof MethodCall) {
			MethodCall methode = (MethodCall) this.value;
//            Instanciation instanciation = (Instanciation) this.type;
			return methode.getType().compatibleWith(this.type);
		}else if(this.value instanceof AttributAccess) {
			AttributAccess access = (AttributAccess) this.value;
			Type access_type = access.getType();
//          Instanciation instanciation = (Instanciation) this.type;
			return access.getType().compatibleWith(this.type);
		}else {
			return value.getType().compatibleWith(type);
		}
	}
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;
		return this.type.length();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.add(_factory.createPush(this.getType().length()));
		System.out.println(value.getClass());
		frag.append(value.getCode(_factory));
		System.out.println(this.name);

		frag.add(_factory.createStore(this.getRegister(), this.getOffset(), this.getType().length()));
		return frag;
	}

}

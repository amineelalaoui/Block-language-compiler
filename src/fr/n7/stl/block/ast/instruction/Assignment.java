/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import com.sun.javafx.tools.packager.Param;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.accessible.ParameterAccess;
import fr.n7.stl.block.ast.expression.accessible.VariableAccess;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.call.AttributAssignement;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for an array type.
 * @author Marc Pantel
 *
 */
public class Assignment implements Instruction, Expression {

	Register register;
	int offset;

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public AssignableExpression getAssignable() {
		return assignable;
	}

	public void setAssignable(AssignableExpression assignable) {
		this.assignable = assignable;
	}

	protected Expression value;
	protected AssignableExpression assignable;

	/**
	 * Create an assignment instruction implementation from the assignable expression
	 * and the assigned value.
	 * @param _assignable Expression that can be assigned a value.
	 * @param _value Value assigned to the expression.
	 */
	public Assignment(AssignableExpression _assignable, Expression _value) {
		this.assignable = _assignable;
		this.value = _value;
		/* This attribute will be assigned to the appropriate value by the resolve action */
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assignable + " = " + this.value.toString() + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.assignable.collectAndPartialResolve(_scope) && value.collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.assignable.completeResolve(_scope) && value.completeResolve(_scope);
	}


	/* (non-Javadoc)
         * @see fr.n7.stl.block.ast.expression.Expression#getType()
         */
	@Override
	public Type getType() {
		return assignable.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		if(assignable.getType() instanceof NamedType && value.getType() instanceof AtomicType)
			return true;
		return assignable.getType().compatibleWith(value.getType());
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
//	@Override
//	public int allocateMemory(Register _register, int _offset)
//	{
//
//		if(this.value instanceof IdentifierAccess) {
//			if( ((IdentifierAccess)this.value).getExpression() instanceof VariableAccess) {
//				VariableAccess va = (VariableAccess) ((IdentifierAccess)this.value).getExpression();
//				VariableDeclaration vd = (VariableDeclaration) va.getDeclaration();
//				 vd.allocateMemory(_register, _offset);
//			}
//
//		}
//		this.register = _register;
//		this.offset = _offset;
//
//		return  0;
//	}

	@Override
	public int allocateMemory(Register _register, int _offset)
	{
		if(this.value instanceof IdentifierAccess) {
			if( ((IdentifierAccess)this.value).getExpression() instanceof VariableAccess) {
				VariableAccess va = (VariableAccess) ((IdentifierAccess)this.value).getExpression();
				VariableDeclaration vd = (VariableDeclaration) va.getDeclaration();
				vd.allocateMemory(_register, _offset);
			}else if(((IdentifierAccess)this.value).getExpression() instanceof ParameterAccess) {
				ParameterAccess pa = (ParameterAccess) ((IdentifierAccess)this.value).getExpression();
				ParameterDeclaration pd = (ParameterDeclaration) pa.getDeclaration();
				pd.setRegister(_register);
//                pd.allocateMemory(_register, _offset);
			}
		}
		if(this.assignable instanceof AttributAssignement) {
			this.register = ((AttributAssignement) this.assignable).getAttribut().getRegister();
			this.offset = ((AttributAssignement) this.assignable).getAttribut().getOffset();
		}else {
			this.register = _register;
			this.offset = _offset;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		fragment.append(value.getCode(_factory));
		fragment.append(assignable.getCode(_factory));
		fragment.add(_factory.createStoreI(value.getType().length()));
		return fragment;
	}

}

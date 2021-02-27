/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import java.util.Optional;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;
	protected Register register;
	protected int offset;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean result = false;
		if(this.condition.collectAndPartialResolve(_scope)){
			result = this.thenBranch.collectAndPartialResolve(_scope) && (elseBranch==null || elseBranch.collectAndPartialResolve(_scope));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if(this.condition.completeResolve(_scope)){
			boolean result = this.thenBranch.completeResolve(_scope);
			return this.elseBranch!=null ? result && this.elseBranch.completeResolve(_scope) : result;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
//		return condition.getType().compatibleWith(AtomicType.BooleanType) && thenBranch.checkType() && (elseBranch==null || elseBranch.checkType());
		Type tvalue = this.condition.getType();
		if(tvalue instanceof PointerType) {
			tvalue = ((PointerType) this.condition.getType()).getPointedType();
		}
		boolean ok1 = tvalue.compatibleWith(AtomicType.BooleanType);
		boolean ok2 = this.thenBranch.checkType();
		if(ok1) {
			if(this.elseBranch != null) {
				return ok2 && this.elseBranch.checkType();
			}else {
				return ok2;
			}
		}else {
				Logger.error(this.condition + " n'est pas un boolean ! ");
				return false;
			}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;

		this.thenBranch.allocateMemory(_register,_offset);
		if(elseBranch !=null)
			elseBranch.allocateMemory(_register,_offset);
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		int idCond = _factory.createLabelNumber();
		System.out.println(condition == null);
		frag.append(this.condition.getCode(_factory));
		frag.add(_factory.createJumpIf("elseBranch"+ idCond,0));
		frag.append(thenBranch.getCode(_factory));
		frag.add(_factory.createJump("endCondition"+idCond));
		frag.addSuffix("elseBranch"+idCond);
		if(elseBranch !=null) {
			frag.append(elseBranch.getCode(_factory));
		}
		frag.addSuffix("endCondition"+idCond);
		return frag;
 }
}

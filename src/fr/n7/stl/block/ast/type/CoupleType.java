/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Sequence;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/**
 * Implementation of the Abstract Syntax Tree node for a couple type.
 * @author Marc Pantel
 *
 */
public class CoupleType implements Type {

	private Type first;
	private Type second;

	/**
	 * Builds a couple type.
	 * @param _first Type of the first part of the couple.
	 * @param _second Type of the second part of the couple.
	 */
	public CoupleType(Type _first, Type _second) {
		this.first = _first;
		this.second = _second;
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.Type#equalsTo(fr.n7.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof CoupleType) {
			return this.first.equalsTo(((CoupleType) _other).first)
					&& this.second.equalsTo(((CoupleType) _other).second);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.Type#compatibleWith(fr.n7.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof CoupleType) {
			return this.first.compatibleWith(((CoupleType) _other).first)
					&& this.second.compatibleWith(((CoupleType) _other).second);
		}
		else if(_other instanceof SequenceType){
			if(((SequenceType) _other).getTypes().size()!=2)
				throw new SemanticsUndefinedException(" Sequence must have two elements ");
			else {
				boolean _first = first.compatibleWith(((SequenceType) _other).getTypes().get(0));
				if(!_first)
					throw new SemanticsUndefinedException("Type mismatched");
				boolean _second = second.compatibleWith(((SequenceType) _other).getTypes().get(1));
				if(!_second)
					throw new SemanticsUndefinedException("Type mismatched");
				return true;
			}
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.Type#merge(fr.n7.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof CoupleType) {
			return new CoupleType( 
					this.first.merge(((CoupleType) _other).first),
					this.second.merge(((CoupleType) _other).second));
		} else {
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length()
	 */
	@Override
	public int length() {
		return this.first.length() + this.second.length();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "< " + this.first + ", " + this.second + ">";
	}

	/**
	 * Provide the type of the first element in the couple.
	 * @return Type of the first element.
	 */
	public Type getFirst() {
		return this.first;
	}

	/**
	 * Provide the type of the second element in the couple.
	 * @return Type of the second element.
	 */
	public Type getSecond() {
		return this.second;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean _first = this.first.completeResolve(_scope);
		boolean _second = this.second.completeResolve(_scope);
		return _first && _second;
	}

}

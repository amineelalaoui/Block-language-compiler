/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Sequence;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/**
 * @author Marc Pantel
 *
 */
public class ArrayType implements Type {

	protected Type element;

	public ArrayType(Type _element) {
		this.element = _element;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof ArrayType) {
			return this.element.equalsTo(((ArrayType)_other).element);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof ArrayType) {
			System.out.println("ArrayType : " + this.element.getClass());
			System.out.println(((ArrayType)_other).element.getClass());
			return this.element.compatibleWith(((ArrayType)_other).element);
		}
		else if(_other instanceof NamedType){
			return this.element.compatibleWith((((NamedType ) _other).getDeclaration().getType()));
		}
		else if(_other instanceof SequenceType){
			boolean _result = true;
			for(int i=0;i<((SequenceType) _other).getTypes().size();i++)
				_result = _result && element.compatibleWith(((SequenceType) _other).getTypes().get(i));
			return _result;
		} else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof ArrayType) {
			return new ArrayType(this.element.merge(((ArrayType)_other).element));
		} else {
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return element.length();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.element + " [])";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.element.completeResolve(_scope);
	}

	/**
	 * @return Type of the elements in the array.
	 */
	public Type getType() {
		return this.element;
	}

}

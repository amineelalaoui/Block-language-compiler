/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

import java.util.function.Function;

/**
 * Implementation of the Abstract Syntax Tree node for a named type.
 * 
 * @author Marc Pantel
 *
 */
public class NamedType implements Type {

	private TypeDeclaration declaration;

	public String name;

	public NamedType(String _name) {
		this.name = _name;
		this.declaration = null;
	}

	public NamedType(TypeDeclaration _declaration) {
		this.declaration = _declaration;
		this.name = _declaration.getName();
	}

	Function<Void,TypeDeclaration> getDeclaration = (Void) -> declaration;

	public TypeDeclaration getDeclaration() {
		return declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof NamedType) {
			return (this.declaration.getName().equals(((NamedType) _other).declaration.getName()));
		} else {
			return (this.declaration.getType().equalsTo(_other));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof NamedType) {
			if(name.equals(((NamedType) _other).name)){
				declaration = ((NamedType) _other).declaration ;
				return true;
			}
			return (this.declaration.getName().equals(((NamedType) _other).declaration.getName()));
		}
		else if(_other instanceof AtomicType) {
			if(this.getDeclaration().getType() instanceof NamedType)
				return _other.equalsTo(((NamedType) this.getDeclaration().getType()).getDeclaration().getType());
			return _other.equalsTo(this.getDeclaration().getType());
		}
		return (this.declaration.getType().compatibleWith(_other));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof NamedType) {
			if (this.declaration.getName().equals(((NamedType) _other).declaration.getName())) {
				return this;
			} else {
				return AtomicType.ErrorType;
			}
		} else {
			return (this.declaration.getType().merge(_other));
		}
	}

	/**
	 * Provide the target type of the named type (i.e. type associated to the name).
	 * 
	 * @return Type associated to the name.
	 */
	public Type getType() {
		Type _result = this.declaration.getType();
		if (_result instanceof NamedType) {
			return ((NamedType) _result).getType();
		} else {
			return _result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		System.out.println(name);
		return this.declaration.getType().length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		if (this.declaration == null) {
			if (_scope.contains(this.name)) {
				try {
					TypeDeclaration _declaration = (TypeDeclaration) _scope.get(this.name);
					this.declaration = _declaration;
					return true;
				} catch (ClassCastException e) {
					Logger.error("The declaration for " + this.name + " is of the wrong kind.");
					return false;
				}
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;
			}
		} else {
			return true;
		}
	}

}

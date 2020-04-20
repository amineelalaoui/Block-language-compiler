/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected Expression target;
	protected Type type;
	protected String name;

	public AbstractConversion(Expression _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(Expression _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {

		return this.type;

	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if(this.type == null) {
			if (_scope.knows(this.name)) {
				Declaration declaration = _scope.get(this.name);

				if (declaration instanceof TypeDeclaration) {
					this.type = ((TypeDeclaration) declaration).getType();
					return this.target.collectAndPartialResolve(_scope);
				} else {
					Logger.error(this.name + " is not a declared type.");
					this.target.collectAndPartialResolve(_scope);
					return false;

				}
			} else {
				Logger.error(this.name + " is not a declared type.");
				this.target.collectAndPartialResolve(_scope);
				return false;
			}
		}
		return this.target.collectAndPartialResolve(_scope);

	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if(this.type == null) {
			if (_scope.knows(this.name)) {
				Declaration declaration = _scope.get(this.name);

				if (declaration instanceof TypeDeclaration) {
					this.type = ((TypeDeclaration) declaration).getType();
					System.out.println("abstract conversion 888 "+this.target.completeResolve(_scope));
					return this.target.completeResolve(_scope);
				} else {
					Logger.error(this.name + " is not a declared type.");
					this.target.completeResolve(_scope);
					return false;

				}
			} else {
				Logger.error(this.name + " is not a declared type.");
				this.target.completeResolve(_scope);
				return false;
			}
		}
			 return this.target.completeResolve(_scope);

			}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {

			return this.target.getCode(_factory);
	}

}

package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractField implements Expression {

	protected Expression record;
	protected String name;
	protected FieldDeclaration field;
	
	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(Expression _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	public FieldDeclaration getField() {
		// TODO problem to access ext1 from here
		if(field == null){
			Type _recordType = record.getType();
			if(_recordType instanceof RecordType)
				return ((RecordType) _recordType).get(name);
			else if(_recordType instanceof NamedType){
				TypeDeclaration _typeDeclaration = ((NamedType) _recordType).getDeclaration();
				if(_typeDeclaration.getType() instanceof RecordType) {
					System.out.println(((RecordType) _typeDeclaration.getType()).get(name));
					System.out.println(name);
					System.out.println((RecordType) _typeDeclaration.getType());
					return ((RecordType) _typeDeclaration.getType()).get(name);
				}
				else if(_typeDeclaration.getType() instanceof NamedType) {
					System.out.println(((RecordType) ((NamedType) _typeDeclaration.getType()).getDeclaration().getType()).get(name));
					return ((RecordType) ((NamedType) _typeDeclaration.getType()).getDeclaration().getType()).get(name);
				}
				else
					throw new SemanticsUndefinedException("undefined instance of TypeDeclaration.getType() : found " + _typeDeclaration.getType().getClass());
			}

		}

		return field;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.record + "." + this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.record.collectAndPartialResolve(_scope) ;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.record.completeResolve(_scope);
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		return record.getType();
	}

	public Expression getRecord() {
		return record;
	}
}

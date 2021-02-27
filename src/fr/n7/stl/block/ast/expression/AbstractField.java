package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.block.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import java.util.*;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.poo.call.ConstructorCall;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.declaration.PooDeclaration;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.poo.definition.Definition;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.util.Logger;

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
	protected Attribut attribut;

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(Expression _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	public AbstractField() {
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
					if(((RecordType) _typeDeclaration.getType()).get(name) == null){
						List<FieldDeclaration> _fields = ((RecordType) _typeDeclaration.getType()).getFields();
						for(FieldDeclaration _field : _fields){
							if(_field.getType() instanceof NamedType){
								if(((RecordType) ((NamedType) _field.getType()).getDeclaration().getType()).get(name) !=null)
									return ((RecordType) ((NamedType) _field.getType()).getDeclaration().getType()).get(name);
							}
						}
						throw new SemanticsUndefinedException("field " + name + " is not declared");
					}
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
		if(this.record instanceof IdentifierAccess) {
			IdentifierAccess access = (IdentifierAccess) this.record;
			Declaration _declaration = _scope.get(access.name);

			if(_declaration instanceof Attribut) {
				Attribut attribut = (Attribut) _declaration;
				this.attribut = attribut;
				access.setAttribut(attribut);
				ClasseDeclaration cd = (ClasseDeclaration) ((ConstructorCall)attribut.getExpression()).getInstanciation().getDeclaration().getContainer();

				for(Definition d : cd.getDefinitionList()) {
					if(d.getAttribut() != null) {
						if(d.getAttribut().getIdent().equals(this.name)) {
							return true;
						}
					}
				}
				return true;
			}else if(_declaration instanceof VariableDeclaration) {
				VariableDeclaration variable = (VariableDeclaration) _declaration;
				if(variable.getType() instanceof Instanciation) {
					ClasseDeclaration cd = (ClasseDeclaration) ((Instanciation)variable.getType()).getDeclaration().getContainer();

					for(Definition d : cd.getDefinitionList()) {
						if(d.getAttribut() != null) {
							if(d.getAttribut().getIdent().equals(this.name)) {
								this.attribut = d.getAttribut();
								access.setAttribut(d.getAttribut());
								return true;
							}
						}
					}
				}
			}else {
				return this.record.completeResolve(_scope);
			}

		}else if(this.record instanceof Instanciation) {
			Declaration _declaration = _scope.get(((Instanciation) this.record).getName());

			if(_declaration instanceof PooDeclaration) {
				((Instanciation) this.record).setDeclaration((PooDeclaration) _declaration);
				List<Definition> definitions = ((PooDeclaration) _declaration).getDefinitions();
				for(Definition def : definitions) {
					if(def.getName().equals(this.name)) {

						if(def.getAttribut() != null) {
							this.attribut = def.getAttribut();
							return true;
						}
					}
				}
				Logger.error(this.name + "Not found in " + this.record.toString());
				return false;
			}
		}else if(this.record instanceof VariableAssignment) {
			String name = ((VariableAssignment) this.record).name;
			Declaration _declaration = _scope.get(name);

			if(_declaration instanceof VariableDeclaration) {
				((VariableAssignment) this.record).setDeclaration((VariableDeclaration) _declaration);
				VariableDeclaration variable = (VariableDeclaration) _declaration;
				if(variable.getType() instanceof Instanciation) {
					ClasseDeclaration cd = (ClasseDeclaration) ((Instanciation)variable.getType()).getDeclaration().getContainer();

					for(Definition d : cd.getDefinitionList()) {
						if(d.getAttribut() != null) {
							if(d.getAttribut().getIdent().equals(this.name)) {
								this.attribut = d.getAttribut();
								if(this.attribut.isFinal()) {
									Logger.error("you cant modify the attribut "+this.attribut.getName()+" !!");
								}
//								access.setAttribut(d.getAttribut());
								return true;
							}
						}
					}
				}
			}
		}
		return this.record.completeResolve(_scope);
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		if(this.record instanceof Instanciation) {
			for (final Definition definition : ((ClasseDeclaration)((Instanciation)this.record).getDeclaration().getContainer()).getDefinitionList()) {
				if (definition.getAttribut() != null) {
					if(definition.getAttribut().getName().equals(this.name)) {
						if(definition.getAttribut().isStatic())
							return definition.getAttribut().getType();
						else
							return null;
					}
				}
			}
		}else if(this.record instanceof IdentifierAccess) {
			Attribut attribut = ((IdentifierAccess) this.record).getAttribut();
			if(attribut.getType() instanceof Instanciation) {
				for (Definition definition : ((ClasseDeclaration)((Instanciation)attribut.getType()).getDeclaration().getContainer()).getDefinitionList()) {
					if (definition.getAttribut() != null) {
						if(definition.getAttribut().getName().equals(this.name)) {
							return definition.getAttribut().getType();
						}

					}
				}
			}else {
				return attribut.getType();
			}

		}else if(this.record instanceof VariableAssignment) {
			if(this.attribut != null) {
				return this.attribut.getType();
			}
		}else {
			return this.record.getType();
		}
		return (Type)AtomicType.ErrorType;
	}

	public Expression getRecord() {
		return record;
	}
}

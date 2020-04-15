/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField implements Expression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _record, String _name) {
		super(_record, _name);
		System.out.println(record.getClass() + ":" + name);
		//System.exit(0);
	}



	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int length = 0;
		Fragment _fragment = _factory.createFragment();
		// TODO check the record class name before doing cast
		if(record instanceof IdentifierAccess)
			length = ((RecordType)((VariableAccess)((IdentifierAccess) record).expression).declaration.getType()).get(name).getType().length();
		else{
			FieldAccess _field = (FieldAccess) record;
			IdentifierAccess _id =(IdentifierAccess)  _field.record;
			NamedType _type = (NamedType) _id.getType();
			RecordType _recType = (RecordType) _type.getDeclaration().getType();
			if(_recType.get(name) == null){
				List<FieldDeclaration> _fields = _recType.getFields();
				if(_fields.contains(name))
					length= _fields.get(_fields.indexOf("name")).getType().length();
				else {
					for (FieldDeclaration field : _fields) {
						if(field.getType() instanceof RecordType)
							length = ((RecordType) field.getType()).get(name).getType().length();
						else if(field.getType() instanceof NamedType){
							if(((NamedType) field.getType()).getDeclaration().getType() instanceof RecordType)
								length = ((RecordType) ((NamedType) field.getType()).getDeclaration().getType()).get(name).getType().length();
						}
					}
				}
			}
		}
		_fragment.append(this.record.getCode(_factory));
		_fragment.add(_factory.createPop(0, 0));
		_fragment.add(_factory.createPop(length, this.record.getType().length() - length ));
		return _fragment;
	}

	@Override
	public Type getType() {
		//TODO : Bug fix -> check instance of (getRecord & expression) if they are an instance of VariableAccess or IdentifierAccess
		if(record instanceof IdentifierAccess)
			return ((RecordType)((VariableAccess)((IdentifierAccess) record).expression).declaration.getType()).get(name).getType();
		else if(record instanceof FieldAccess){
			FieldAccess _field = (FieldAccess) record;
			IdentifierAccess _id =(IdentifierAccess)  _field.record;
			NamedType _type = (NamedType) _id.getType();
			RecordType _recType = (RecordType) _type.getDeclaration().getType();
			if(_recType.get(name) == null){
				List<FieldDeclaration> _fields = _recType.getFields();
				if(_fields.contains(name))
					return _fields.get(_fields.indexOf("name")).getType();
				else {
					for (FieldDeclaration field : _fields) {
						if(field.getType() instanceof RecordType)
							return ((RecordType) field.getType()).get(name).getType();
						else if(field.getType() instanceof NamedType){
							if(((NamedType) field.getType()).getDeclaration().getType() instanceof RecordType)
								return ((RecordType) ((NamedType) field.getType()).getDeclaration().getType()).get(name).getType();
						}
					}
				}
			}
			else
				return _recType.get(name).getType();
		}
		throw new SemanticsUndefinedException(getType() + " missmatched");
	}
}

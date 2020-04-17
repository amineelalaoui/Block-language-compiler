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

import java.util.Collections;
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
	}



	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int length = 0;
		boolean _found = false;
		int _lengthBypass = 0;
		Fragment _fragment = _factory.createFragment();
		_fragment.append(this.record.getCode(_factory));
		// record can be an instance of IdentifierAccess or FieldAccess
		// Field access can have a recursive NamedType
		if(record instanceof IdentifierAccess){
			System.out.println("Identifier Access");
			if(((VariableAccess) ((IdentifierAccess) record).expression).declaration.getType() instanceof RecordType) {
				RecordType _record = (RecordType) ((VariableAccess) ((IdentifierAccess) record).expression).declaration.getType();
				length = _record.get(name).getType().length();
				List<FieldDeclaration> _fields = _record.getFields();
				for (int i = _fields.size() - 1; i >= 0; i--) {
					System.out.println(_fields.get(i).getName());
					if (!_fields.get(i).getName().equals(name))
						_lengthBypass += _fields.get(i).getType().length();
					else
						break;
				}
				System.out.println("Bypass value " + _lengthBypass);
			}
			else{
				//we're dealing here with an instance of NamedType
				NamedType _namedType = (NamedType) ((VariableAccess) ((IdentifierAccess) record).expression).declaration.getType();
				RecordType _recTypr = (RecordType) _namedType.getDeclaration().getType();
				System.out.println("fields of " + _namedType.getDeclaration().getName());
				_recTypr.getFields().forEach(System.out::println);
				FieldDeclaration _target = _recTypr.get(name);
				if(_target ==null)
					throw new SemanticsUndefinedException(name + " is undefined in " + _recTypr.getName());
				else{
					length =_target.getType().length();
					List<FieldDeclaration> _fields = _recTypr.getFields();
					for (int i = _fields.size() - 1; i >= 0; i--) {
						if (!_fields.get(i).getName().equals(name))
							_lengthBypass += _fields.get(i).getType().length();
						else
							break;
					}
				}
			}
		}
		else{
			// record now is an instance of FieldAccess
			// check if we're dealing with an instance of RecordType or NamedType ...
			IdentifierAccess _idAccess = ((IdentifierAccess) ((FieldAccess) record).record);
			if(_idAccess.expression.getType() instanceof NamedType){
				// get the record type from the namedType
				RecordType _record = ((RecordType) ((NamedType) _idAccess.expression.getType()).getDeclaration().getType());
				List<FieldDeclaration> _fields = _record.getFields();
				for(int i=_fields.size()-1; i>=0;i--){
					//getting fields from the field NamedType
					RecordType _fieldRecType = (RecordType) ((NamedType) _fields.get(i).getType()).getDeclaration().getType();
					List<FieldDeclaration> _fieldDeclaration = _fieldRecType.getFields();
					for(int j=_fieldDeclaration.size()-1;j>=0;j--){
						int _localLength = _fieldDeclaration.get(j).getType().length();
						if(_fieldDeclaration.get(j).getName().equals(name)){
							length = _localLength;
							break;
						}
						else
							_lengthBypass+=_localLength;
					}
					if(length!=0) break;
				}
				if(length==0)
					throw new SemanticsUndefinedException(name + " is undefined in " + _record.getName());
			}
			else{
				// we're dealing with a RecordType instead of NamedType
				RecordType _record = (RecordType) _idAccess.expression.getType();
				if(!_record.get(name).getName().equals(name))
					throw new SemanticsUndefinedException(name + " is undefined in " + _record.getName());
				var _fields = _record.getFields();
				Collections.reverse(_fields);
				for(int i=_fields.size()-1; i>=0;i--){
					int _localLength = _fields.get(i).getType().length();;
					if(_fields.get(i).getName().equals(name)){
						length = _localLength;
						break;
					}
					else
						_lengthBypass+=_localLength;
				}
			}

		}
		_fragment.add(_factory.createPop(0, _lengthBypass));
		_fragment.add(_factory.createPop(length, this.record.getType().length() - length - _lengthBypass ));
		return _fragment;
	}

	@Override
	public Type getType() {
		if(record instanceof IdentifierAccess) {
			if(((VariableAccess) ((IdentifierAccess) record).expression).declaration.getType() instanceof RecordType)
				return ((RecordType) ((VariableAccess) ((IdentifierAccess) record).expression).declaration.getType()).get(name).getType();
			else{
				//instanceof namedtype
				VariableAccess _access = ((VariableAccess) ((IdentifierAccess) record).expression);
				NamedType _namedType = (NamedType) _access.declaration.getType();
				RecordType _rectype = (RecordType) _namedType.getType();
				if(_rectype.get(name)!=null)
						return _rectype.get(name).getType();
				else{
					List<FieldDeclaration> _fields = _rectype.getFields();
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
			}
		}
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

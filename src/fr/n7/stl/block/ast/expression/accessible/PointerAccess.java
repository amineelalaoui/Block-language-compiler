/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractPointer;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.FunctionCall;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.lang.reflect.Field;

/**
 * Implementation of the Abstract Syntax Tree node for a pointer access expression.
 * @author Marc Pantel
 *
 */
public class PointerAccess extends AbstractPointer implements Expression {

	/**
	 * Construction for the implementation of a pointer content access expression Abstract Syntax Tree node.
	 * @param _pointer Abstract Syntax Tree for the pointer expression in a pointer content access expression.
	 */
	public PointerAccess(Expression _pointer) {
		super(_pointer);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		VariableDeclaration _varDeclaration;
		Fragment _frag = _factory.createFragment();
		VariableAccess _varAccess;
		if(super.pointer instanceof IdentifierAccess) {
			IdentifierAccess _idAccess = (IdentifierAccess) super.pointer;
			if(_idAccess.expression instanceof VariableAccess) {
				_varAccess = (VariableAccess) _idAccess.expression;
				_varDeclaration = _varAccess.declaration;
				_frag.add(_factory.createLoad(_varDeclaration.getRegister(), _varDeclaration.getOffset(), _varDeclaration.getType().length()));
				_frag.add(_factory.createLoadI(_varDeclaration.getType().length()));
			}
			else{
				// instanceof PointerAccess
				ParameterAccess _paramAccess = (ParameterAccess) _idAccess.expression;
				ParameterDeclaration _paramDecl = _paramAccess.declaration;
				_frag.add(_factory.createLoad(_paramDecl.getRegister(),_paramDecl.getOffset(),_paramDecl.getType().length()));
				_frag.add(_factory.createLoadI(_paramDecl.getType().length()));

			}
		}
		else{
			// FieldAccess
			FieldAccess _idAccess = (FieldAccess) super.pointer;
			FieldDeclaration _fieldAccess = _idAccess.getField();
			_frag.add(_factory.createLoad(((VariableDeclaration) ((IdentifierAccess) _idAccess.getRecord()).expression.getDeclaration()).getRegister(),_fieldAccess.getOffset(),_fieldAccess.getType().length()));
		 	_frag.add(_factory.createLoadI(_fieldAccess.getType().length()));
		}

		return _frag;
	}

}

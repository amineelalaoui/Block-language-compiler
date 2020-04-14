/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractPointer;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

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
		Fragment _frag = _factory.createFragment();
		IdentifierAccess _idAccess = (IdentifierAccess)super.pointer;
		VariableAccess _varAccess = (VariableAccess) _idAccess.expression;
		VariableDeclaration _varDeclaration = _varAccess.declaration;
		_frag.add(_factory.createLoad(_varDeclaration.getRegister(),_varDeclaration.getOffset(),_varDeclaration.getType().length()));
		_frag.add(_factory.createLoadI(_varDeclaration.getType().length()));
		return _frag;
	}

}

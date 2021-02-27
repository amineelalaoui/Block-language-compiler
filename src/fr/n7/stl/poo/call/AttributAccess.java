package fr.n7.stl.poo.call;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributAccess extends AbstractField implements Expression {
    /**
     * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
     *
     * @param _record Abstract Syntax Tree for the record part in a record field access expression.
     * @param _name   Name of the field in the record field access expression.
     */

    public AttributAccess(Expression _record, String _name) {
        super(_record, _name);
    }



    @Override
    public Fragment getCode(TAMFactory _factory) {

        Fragment frag = _factory.createFragment();
        if(this.record instanceof Instanciation || this.record instanceof IdentifierAccess) {
            frag.add(_factory.createLoad(
                    this.attribut.getRegister(),
                    this.attribut.getOffset(),
                    this.attribut.getType().length()));
            frag.addComment(this.toString());
            return frag;
        }else {
            frag = this.record.getCode(_factory);
        }

        //frag.add(_factory.createPop(0, recordLength - attributOffset - attributLength));

       // frag.add(_factory.createPop(attributLength, attributOffset));

        return frag;
    }
}

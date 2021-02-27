package fr.n7.stl.poo.call;

import fr.n7.stl.block.ast.expression.AbstractIdentifier;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributAssignement extends AbstractIdentifier implements AssignableExpression {

    private Attribut attribut;

    public Attribut getAttribut() {
        return attribut;
    }

    public void setAttribut(Attribut attribut) {
        this.attribut = attribut;
    }

    /**
     * Creates a variable related expression Abstract Syntax Tree node.
     *
     * @param _name Name of the variable.
     */
    public AttributAssignement(String _name) {

        super(_name);
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
            Declaration _declaration = _scope.get(this.name);
            if (_declaration instanceof Attribut) {
                this.attribut = ((Attribut) _declaration);
                return true;
            } else {
                Logger.error("The declaration for " + this.name + " is of the wrong kind.");
                return false;
            }
        } else {
            Logger.error("The identifier " + this.name + " has not been found.");
            return false;
        }
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        if(((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
            Declaration _declaration = _scope.get(this.name);

            if (_declaration instanceof Attribut) {
                this.attribut = ((Attribut) _declaration);

                if (this.attribut.isFinal()) {
                    Logger.error("Can't assign final");
                    return false;
                }
                return true;
            } else if(_declaration instanceof VariableDeclaration){
                return this.attribut.getType().equalsTo(_declaration.getType());
            }else if(_declaration instanceof  ParameterDeclaration){
                return this.attribut.getType().equalsTo(_declaration.getType());
            }


            else {
                Logger.error(this.name + " is not an attribute");
                return false;
            }
        }

        else {
            Logger.error(this.name + " has not been found");
            return false;
        }
    }

    @Override
    public Type getType() {
        return this.attribut.getType();
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment frag = _factory.createFragment();
        frag.add(_factory.createLoad(this.attribut.getRegister(),this.attribut.getOffset(),this.attribut.getType().length()));

        return frag;    }
}

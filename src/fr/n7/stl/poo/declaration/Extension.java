package fr.n7.stl.poo.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.type.Instanciation;

public class Extension implements Type {

    Instanciation instanciation;
    public Extension(){

    }

    public Extension(Instanciation instanciation) {
        this.instanciation = instanciation;
    }

    public Instanciation getInstanciation() {
        return instanciation;
    }
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.instanciation.completeResolve(_scope);
    }
    public void setInstanciation(Instanciation instanciation) {
        this.instanciation = instanciation;
    }
    public boolean isPresent() {
        if (this.instanciation == null){
            return false;
        }
        return true;
    }
    @Override
    public boolean equalsTo(Type _other) {
        return this.instanciation.equalsTo(_other);
    }

    @Override
    public boolean compatibleWith(Type _other) {
        return false;
    }

    @Override
    public Type merge(Type _other) {
        return null;
    }
    public Type getType(){
		return this.instanciation.getType();
	}
    @Override
    public int length() {
        return 0;
    }

}

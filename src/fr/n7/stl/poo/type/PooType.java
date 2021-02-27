package fr.n7.stl.poo.type;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public enum PooType implements Type {
        ClassType,
        InterfaceType;

        @Override
        public boolean equalsTo(Type _other) {
            return this == _other;
        }

        @Override
        public boolean compatibleWith(Type _other) {

            return this.equalsTo(_other);
        }

        @Override
        public Type merge(Type _other) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int length() {
            // TODO Auto-generated method stub
            return  1;
        }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }


        public boolean resolve(HierarchicalScope<Declaration> _scope) {
            // TODO Auto-generated method stub
            return true ;
        }
    }

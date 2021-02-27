package fr.n7.stl.poo.declaration;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.*;

public class GenericTypeDeclaration {
    String name;
    List<Instanciation>  instanciationList;


    public GenericTypeDeclaration(String name, List<Instanciation> instanciationList) {
        super();
        this.name = name;
        this.instanciationList = instanciationList;
    }
    public GenericTypeDeclaration(String name){
        super();
        this.name = name;
    }
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        throw new SemanticsUndefinedException("Semantics getCode is not implemented in PointerAccess.");
    }

    public Type getType()
    {
        throw new SemanticsUndefinedException("Semantics getCode is not implemented in PointerAccess.");
    }

    public Fragment getCode(TAMFactory _factory)
    {
        throw new SemanticsUndefinedException("Semantics getCode is not implemented in PointerAccess.");
    }
}

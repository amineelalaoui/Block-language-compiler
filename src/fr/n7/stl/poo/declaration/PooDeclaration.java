package fr.n7.stl.poo.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.poo.methode.Methode;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.poo.definition.Definition;
import fr.n7.stl.poo.type.PooType;

import  java.util.*;

public class PooDeclaration implements Declaration {
    boolean isClasse;
    List<Definition> definitions;
    ContainerDeclaration container;
    String name;
    List<GenericTypeDeclaration> genDeclaration;

    public PooDeclaration(String name) {
        super();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GenericTypeDeclaration> getGenDeclaration() {
        return genDeclaration;
    }

    public void setGenDeclaration(List<GenericTypeDeclaration> genDeclaration) {
        this.genDeclaration = genDeclaration;
    }

    public PooDeclaration(String name, List<GenericTypeDeclaration> genDeclaration) {
        super();
        this.name = name;
        this.genDeclaration = genDeclaration;
    }

    public boolean isClasse() {
        return isClasse;
    }

    public void setClasse(boolean classe) {
        isClasse = classe;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public ContainerDeclaration getContainer() {
        return container;
    }

    public void setContainer(ContainerDeclaration container) {
        this.container = container;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Type getType(){
        if(this.isClasse) return PooType.ClassType;
		return PooType.InterfaceType;
    }

    public List<Attribut> getStaticAttributsOfClass() {

        List<Attribut> attributs = new LinkedList<Attribut>();
        for(Definition i : definitions)
            if(i.getAttribut() != null && i.isStatic())
                attributs.add(i.getAttribut());

        return attributs;
    }

    public List<Methode> getStaticMethodesOfClass() {

        List<Methode> methodes = new LinkedList<Methode>();
        for(Definition i : definitions)
            if(i.getMethode() != null && i.isStatic())
                methodes.add(i.getMethode());

        return methodes;
    }

}

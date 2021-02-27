package fr.n7.stl.poo.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.poo.methode.Constructor;
import fr.n7.stl.block.poo.methode.Methode;
import fr.n7.stl.block.poo.methode.MethodeSignature;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.poo.definition.Definition;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.poo.type.PooType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import org.apache.commons.lang3.EnumUtils;
import sun.rmi.runtime.Log;

import java.util.*;

public class ClasseDeclaration extends ContainerDeclaration implements Type {
    List<Constructor> constructorList = new ArrayList<Constructor>(); //Liste des constrcucteurs

    int finalOrAbstract;
    PooDeclaration pooDeclaration;    // c'est notre classe
    Extension extension;             // la classe extends
    List<Instanciation> implementations;  // les interfaces à implementer
    List<MethodeSignature> methodeSignatureListDansAbstract = new LinkedList<MethodeSignature>();
    //liste des methodes abtraites à implémenter si
    //la classe est abstraite

    public int getFinalOrAbstract() {
        return finalOrAbstract;
    }
    public List<Constructor> getConstructorList() {
        return constructorList;
    }

    public void setConstructorList(List<Constructor> constructorList) {
        this.constructorList = constructorList;
    }
    public void setFinalOrAbstract(int finalOrAbstract) {
        this.finalOrAbstract = finalOrAbstract;
    }

    List<Definition> definitionList;     // les définitions ( methodes, constructeurs, attributs)
    int offset;
    Register register;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public ClasseDeclaration(int finalOrAbstract, PooDeclaration declaration, Extension extension,
                             List<Instanciation> implementatioList, List<Definition> definitions){
        super();
        this.finalOrAbstract = finalOrAbstract;
        this.pooDeclaration = declaration;
        this.extension = extension;
        this.implementations = implementatioList;
        this.definitionList = definitions;
        this.pooDeclaration.setDefinitions(definitions);
        this.pooDeclaration.setContainer(this);
    }

    public Methode getMain()
    {
        for(Definition d : definitionList)
        {
            if(d.getMethode() != null)
            {
                Methode m = d.getMethode();
                if(m.getEntete().getName().equals("main") && m.isStatic() && d.isPublicOrPrivate() && m.getType().equals(AtomicType.VoidType) && m.getEntete().getParameterDeclarations() == null)
                {
                    return m;
                }
                else {
                    return null;
                }
            }
        }

        return null;
    }

    public List<Instanciation> getImplementations() {
        return implementations;
    }

    public void setImplementations(List<Instanciation> implementations) {
        this.implementations = implementations;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    @Override
    public boolean equalsTo(Type _other) {
        return false;
    }

    @Override
    public boolean compatibleWith(Type _other) {
        if(_other.equalsTo(extension.getType()))
            return true;
        for(Instanciation i : this.implementations){
            if (_other.equalsTo(i.getType())) return true;
        }

        return false;
    }

    @Override
    public Type merge(Type _other) {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }

    public boolean resolvePre(HierarchicalScope<Declaration> _scope) {
        if(_scope.knows(this.pooDeclaration.getName())){
            Logger.error("The name " +this.pooDeclaration.getName()+" of this Class is already used ");
            return false;
        }
        this.pooDeclaration.setClasse(true);
        _scope.register(this.pooDeclaration);
        return true;

    }

    public String getName(){
        return this.pooDeclaration.name;
    }
    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {

        boolean result = true;

        // si cette classe est abstraite
        if(this.finalOrAbstract == 1) {
            for(Definition df : this.definitionList){
                if(df.getMethode() != null){
                    if(df.getMethode().getFinalOrAbstract() == 1)
                        methodeSignatureListDansAbstract.add(df.getMethode().getEntete());
                }
            }
        }

        if(this.extension.isPresent()){
            List<MethodeSignature> methodeSignatureListDansAbstract = new LinkedList<MethodeSignature>();
            PooDeclaration p =   (PooDeclaration) _scope.get(extension.instanciation.getName());
            ClasseDeclaration cd = (ClasseDeclaration) p.container;
            extension.instanciation.setDeclaration(p);
            if(cd.finalOrAbstract == 1){
                for(Definition df : cd.definitionList){
                    if(df.getMethode() != null){
                        if(df.getMethode().isDefined==0)
                            methodeSignatureListDansAbstract.add(df.getMethode().getEntete());
                    }
                }
            }
            if(methodeSignatureListDansAbstract.size() != 0){
                int i = methodeSignatureListDansAbstract.size();
                for(Definition definition : this.definitionList){
                    if(definition.getMethode()!=null){
                        boolean found = false;
                        if(methodeSignatureListDansAbstract.contains(definition.getMethode().getEntete())){
                            i--;
                            found = true ;

                        }
                    }
                }
                if(i == 0){
                    System.out.println("ALL ARE IMPLEMENTED");
                }
                else {
                    Logger.error("This Class must implements all method from Abstract Class");

                }
            }
            result = extension.completeResolve(_scope);
        }
        for(Instanciation i : this.implementations){
            result = result && i.completeResolve(_scope);
            //check if all methodes are implemented in the interface
            InterfaceDeclaration id = (InterfaceDeclaration) i.getDeclaration().getContainer();



            for(MethodeSignature ms : id.getEntetes()){
                boolean found = false;
                for(Definition d : this.definitionList){
                    if(d.getMethode() != null) {
                        if(d.getMethode().getEntete().equals(ms)) {
                            found = true;
                        }
                    }

                }
                if(!found)
                    Logger.error("Class must implement all methodes");
            }
        }
        boolean exist = false;
//        /*pour  la classe ou on a la methode main, on peut ne pas avoir le constructeur*/
//        for(Definition def : this.definitionList) {
//        	if(def.getMethode() != null) {
//        		exist = def.getMethode().getEntete().getName().equals("main");
//        	}
//        }
        /*verifier que le constructeur existe*/

        for(Definition def : this.definitionList) {
            if(def.getConstructor() != null) {
                exist = true;
            }
        }
        // si le constructeur n'existe pas , on crée un qui est vide
        if(!exist) {
            Constructor constructor = new Constructor(new Instanciation(this.getName()),null,null);
            this.constructorList.add(constructor);
            this.definitionList.add(new Definition(true,constructor));
//        	Logger.error("Constructor muust be implemented ! ");
//        	return false;
        }
        List<Attribut> attributList = new ArrayList<Attribut>();

//        List<Constructor> constructorList = new ArrayList<Constructor>();
        List<MethodeSignature> methodeSignatureList = new ArrayList<MethodeSignature>();
        HierarchicalScope<Declaration> newSymboleTable = new SymbolTable(_scope);
        for(Definition d : this.definitionList){
            result = result && d.completeResolve(newSymboleTable,methodeSignatureList,constructorList);

        }


        //System.out.println(newSymboleTable.toString());

        return result;
    }

    public List<MethodeSignature> getMethodeSignatureListDansAbstract() {
        return methodeSignatureListDansAbstract;
    }

    public void setMethodeSignatureListDansAbstract(List<MethodeSignature> methodeSignatureListDansAbstract) {
        this.methodeSignatureListDansAbstract = methodeSignatureListDansAbstract;
    }

    public boolean checkType(){
        boolean result = true;
        if(this.extension.instanciation != null){
            Type t1 = this.extension.getType();
            if(!t1.equalsTo(PooType.ClassType)){
                Logger.error("extends must be used with class");
                return false;
            }
        }
        for(Instanciation i : this.implementations){
            Type t2 = i.getType();
            if(!t2.equalsTo(PooType.InterfaceType)){
                Logger.error("implements must be used with Interface");
                return false;
            }
        }
        for(Definition d: this.definitionList){
            result = d.checkType() && result;
        }



        return result;
    }
    public List<Definition> getDefinitionList() {
        return definitionList;
    }

    public void setDefinitionList(List<Definition> definitionList) {
        this.definitionList = definitionList;
    }

    public Type getType(){
        return PooType.ClassType;

    }
    public List<Methode> getMethodsOfClass() {
        List<Methode> methods = new LinkedList<Methode>();
        for(Definition i : definitionList)
            if(i.getMethode() instanceof Methode)
                methods.add((Methode) i.getMethode());
        return methods;
    }
    public Fragment getCode(TAMFactory _factory){

        List<Methode> methodeList = new LinkedList<Methode>();
        List<Constructor> constructorList = new LinkedList<Constructor>();

        Fragment frag = _factory.createFragment();
        for(Definition d: this.definitionList)
        {
            if(d.getAttribut() != null )
            {
                frag.append(d.getAttribut().getCode(_factory));
            }
            else if(d.getConstructor() != null)
            {
                frag.append(d.getConstructor().getCode(_factory));

                // frag.add(_factory.createHalt());
            }
            else if(d.getMethode() != null){
                //frag.append(d.getMethode().getCode(_factory));
                if(d.getMethode().getName().equals("main")){
                    frag.append(d.getMethode().getCode(_factory));
                    frag.add(_factory.createHalt());
                }
                else
                     methodeList.add(d.getMethode());
            }

        }
//        if(constructorList.size()!=0)
//            for (Constructor c : constructorList){
//                frag.append(c.getCode(_factory));
//                frag.add(_factory.createHalt());
//            }
        if(methodeList.size() != 0)
        for(Methode m : methodeList){
            frag.append(m.getCode(_factory));
           // frag.add(_factory.createHalt());
        }
        return frag;
    }

    public int allocateMemory(Register register, int offset){

       this.register = register;
       this.offset = offset;

       int size = offset;

        for (Definition d: this.definitionList){
            size += d.allocateMemory(register,size);
        }
        return size;
    }

}

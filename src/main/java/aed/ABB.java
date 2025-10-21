package aed;

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    private int _cardinal;
    private Nodo _raiz;
    private int _altura;


    // Agregar atributos privados del Conjunto

    private class Nodo {
        T valor; 
        Nodo izq; 
        Nodo der; 
        Nodo padre;
        
        Nodo (T v) {valor = v; izq = null; der = null; padre = null;}

        // Agregar atributos privados del Nodo

        // Crear Constructor del nodo
    }

    public ABB() {
        _raiz = null; // es ABB. lo arranco nulo. 
        _cardinal = 0; 
        _altura = 0;
    }

    public int cardinal() {
        return _cardinal;
    }

    public T minimo(){
        if (_raiz == null) {
            return null; // El árbol está vacío
        } else {
            Nodo actual = _raiz;
            while (actual.izq != null){
                actual = actual.izq; 
            }
            return actual.valor; 
        }    
    }

    public T maximo(){
        if (_raiz == null){
            return null; 
        } else {
            Nodo actual = _raiz;  
            while(actual.der != null){
                actual = actual.der; 
            }
            return actual.valor;
        }
    }

    public void insertar(T elem){
        _raiz = insertarRecursivo(_raiz, elem); 
    }

    private Nodo insertarRecursivo(Nodo nodo, T elem){
        if (nodo == null){
            _cardinal++; 
            return new Nodo(elem); 
        }
        if (nodo.valor.compareTo(elem) > 0){
            nodo.izq = insertarRecursivo(nodo.izq, elem); 
            nodo.izq.padre = nodo; 
        } else if (nodo.valor.compareTo(elem) < 0){
            nodo.der = insertarRecursivo(nodo.der, elem); 
            nodo.der.padre = nodo; 
        }
        return nodo; 
    }

    public boolean pertenece(T elem){
        boolean res = false;
        if (_raiz == null){
            return res; 
        } else {
            res = perteneceRecursivo(_raiz, elem); 
        }
        return res;
    }
        
    private boolean perteneceRecursivo(Nodo nodo, T elem){
        boolean res = true; 

        if (nodo == null){// está vacío 
            res = false;
            return res; 
        } else if (nodo.valor.compareTo(elem) == 0){ // lo encontré
            return res;  
        } else if (nodo.valor.compareTo(elem) > 0){ // es menor
            res = perteneceRecursivo(nodo.izq, elem);  
        } else if (nodo.valor.compareTo(elem) < 0){ // es mayor 
            res = perteneceRecursivo(nodo.der, elem);
        }
        return res;
    }

    public void eliminar(T elem){
        // 0. no está. no hago nada 
        boolean estaElemento = pertenece(elem); 
        // TENGO QUE VER CUANDO QUIERO ELIMINAR LA RAIZ¡!!¡!¡


        if (estaElemento){
            // si es la raiz
            if (_raiz.valor.compareTo(elem) == 0){
                eliminarRaiz(_raiz, elem);
                return;
            }

            //  no es la raiz, busco al padre
            Nodo padre = buscarPadreRecursivo(_raiz, elem);
            if (padre == null){
                return;
            }
            Nodo nodoAEliminar = buscarNodoAEliminar(padre, elem);

            int hijos = cantHijos(nodoAEliminar);

            
            if (hijos == 0){// 1. no tiene hijos
                eliminarConCeroHijos(padre,elem);
            } else if (hijos == 1){ //2. tengo 1 hijo
                eliminarConUnHijo(padre, nodoAEliminar,elem); 
            } else {
                eliminarConDosHijos(padre, nodoAEliminar, elem);
            }
        }
    }
    
    private void eliminarConDosHijos (Nodo padre, Nodo nodoAEliminar, T elem){
        Nodo sucesor = buscarSucesor(nodoAEliminar);
        Nodo hijoDerSucesor = sucesor.der;

        // acomodo el lugar donde estaba el sucesor 
        if (sucesor.padre.izq == sucesor){
            sucesor.padre.izq = hijoDerSucesor;
        } else {
            sucesor.padre.der = hijoDerSucesor;
        }

        if (hijoDerSucesor != null){
            hijoDerSucesor.padre = sucesor.padre;
        }

        // acomodo el lugar que saqué 
        if (esHijoIzquierdo(padre, elem)){
            padre.izq = sucesor;
        } else {
            padre.der = sucesor;
        }

        // el sucesor se queda con los hijos del que elimino
        sucesor.der = nodoAEliminar.der;
        sucesor.izq = nodoAEliminar.izq;

        // los hijos del que eliminé, les  pongo como padre al sucesor 
        if (sucesor.izq != null){
            sucesor.izq.padre = sucesor;
        }
        if (sucesor.der != null){
            sucesor.der.padre = sucesor;
        }

        // el padre del sucesor es el padre del que eliminé 
        sucesor.padre = padre;

        _cardinal--;
    }



    // busco el sucesor:
    private Nodo buscarSucesor(Nodo nodo) {
        Nodo actual = nodo.der;
        while (actual != null && actual.izq != null) {
            actual = actual.izq;
        }
        return actual;
    }

    private void eliminarRaiz (Nodo raiz, T elem){
        int hijos = cantHijos(raiz);

        if (hijos == 0){
            _raiz = null;
        } else if (hijos == 1){
            if (_raiz.izq != null){
                _raiz = _raiz.izq; 
            } else {
                _raiz =_raiz.der;
            }
            _raiz.padre = null;
        } else {
            Nodo sucesor = buscarSucesor(_raiz);
            Nodo hijoDeSucesor = sucesor.der;

            // saco el sucesor de su lugar 
            if (sucesor.padre.izq == sucesor){
                sucesor.padre.izq = hijoDeSucesor;
            } else {
                sucesor.padre.der = hijoDeSucesor;
            }

            if (hijoDeSucesor != null){
                hijoDeSucesor.padre = sucesor.padre;
            }

            // sucesor es mi nueva raiz 
            sucesor.izq  = _raiz.izq;
            sucesor.der = _raiz.der;
            sucesor.padre = null;
            _raiz = sucesor;

            // acomodo el resto de padre con los hijos 
            if (_raiz.izq != null){
                _raiz.izq.padre = _raiz;
            } 
            if (_raiz.der != null){
                _raiz.der.padre = _raiz;
            }
        }
        _cardinal--;
    }

    private void eliminarConUnHijo(Nodo padre, Nodo nodoAEliminar, T elem){
        boolean ladoNodoAEliminar = esHijoIzquierdo(padre, elem);

        if (ladoNodoAEliminar) {
            // es más chico
            if (nodoAEliminar.izq != null){
                padre.izq = nodoAEliminar.izq;
            } else { 
                padre.izq = nodoAEliminar.der;
            }
            // siempre lo de la izq va a ser menor. 
            // no me tengo que fijar si lo que conecto es más grande o no que el padre.
            // lo que si veo es dónde está el hijo del que voy a sacar
        } else {
            if (nodoAEliminar.izq != null){
                padre.der = nodoAEliminar.izq;
            } else { 
                padre.der = nodoAEliminar.der;
            }
        }
        
        _cardinal--;
        
    }
    
    private void eliminarConCeroHijos(Nodo padre, T elem){
        if (esHijoIzquierdo(padre, elem)) {
            padre.izq = null; 
        } else {
            padre.der = null;
        }
        _cardinal--;
    }



    private Nodo buscarNodoAEliminar(Nodo padre, T elem){
        Nodo aEliminar = null;

        if (esHijoIzquierdo(padre,elem)){
            aEliminar = padre.izq;
        } else {
            aEliminar = padre.der;
        }
        return aEliminar;
    }

    private boolean esHijoIzquierdo(Nodo padre, T elem){
        return padre.izq != null && padre.izq.valor.compareTo(elem) == 0;
    }

    // busco al padre 
    private Nodo buscarPadreRecursivo(Nodo nodo, T elem){ 
        Nodo padre = nodo;

        if (nodo == null) {
            padre = null;
        } else if ((nodo.izq != null && nodo.izq.valor.compareTo(elem) == 0) || (nodo.der != null && nodo.der.valor.compareTo(elem) == 0)){
            padre = nodo;
        } else if (nodo.valor.compareTo(elem) > 0){
            padre = buscarPadreRecursivo(nodo.izq, elem);
        } else if (nodo.valor.compareTo(elem) < 0){
            padre = buscarPadreRecursivo(nodo.der, elem); 
        }
        return padre;
    } 
   

    // cantidad de hijos de un nodo
    private int cantHijos(Nodo nodo) {
        int totalHijos = 0;
        
        if (nodo.izq == null && nodo.der == null) {
            totalHijos = 0;
        } else if (nodo.izq == null || nodo.der == null) {
            totalHijos = 1;
        } else {
            totalHijos = 2;
        }
        
        return totalHijos;
    }

    public String toString(){
        throw new UnsupportedOperationException("No implementada aun");
    }

    public class ABB_Iterador {
        private Nodo _actual;

        public boolean haySiguiente() {            
            throw new UnsupportedOperationException("No implementada aun");
        }
    
        public T siguiente() {
            throw new UnsupportedOperationException("No implementada aun");
        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }

}

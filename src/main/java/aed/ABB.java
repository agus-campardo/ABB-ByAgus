package aed;

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    private int _cardinal;
    private Nodo _raiz;
    private int _altura;

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
            return null; 
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

        if (nodo == null){
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

        if (pertenece(elem)){

            // 1. Es la raiz
            if (_raiz.valor.compareTo(elem) == 0){
                eliminarRaiz();

            } else {
            //  2. No es la raiz 
            Nodo padre = buscarPadreRecursivo(_raiz, elem);
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
    }

    // AUXILIARES

    // A #1: Eliminar raiz
    private void eliminarRaiz (){
        int hijos = cantHijos(_raiz);

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

            // saco el sucesor de su lugar 
            desconectarSucesor(sucesor);

            // sucesor es mi nueva raiz 
            sucesor.izq  = _raiz.izq;
            sucesor.der = _raiz.der;
            sucesor.padre = null;
            _raiz = sucesor;

        if (_raiz.izq != null) {
            _raiz.izq.padre = _raiz;
        }
        if (_raiz.der != null) {
            _raiz.der.padre = _raiz;
        }
        }
        _cardinal--;
    }

    // # 2: Eliminar sin hijos
        private void eliminarConCeroHijos(Nodo padre, T elem){
        if (esHijoIzquierdo(padre, elem)) {
            padre.izq = null; 
        } else {
            padre.der = null;
        }
        _cardinal--;
    }

    // # 3: Eliminar con un hijo 
        private void eliminarConUnHijo(Nodo padre, Nodo nodoAEliminar, T elem){
        boolean ladoNodoAEliminar = esHijoIzquierdo(padre, elem);

        if (ladoNodoAEliminar) {
            // es más chico
            if (nodoAEliminar.izq != null){
                padre.izq = nodoAEliminar.izq;
                padre.izq.padre = padre;
            } else { 
                padre.izq = nodoAEliminar.der;
                if (padre.izq != null){
                    padre.izq.padre = padre;
                }
            }
            // siempre lo de la izq va a ser menor. 
            // no me tengo que fijar si lo que conecto es más grande o no que el padre.
            // lo que si veo es dónde está el hijo del que voy a sacar
        } else {
            if (nodoAEliminar.izq != null){
                padre.der = nodoAEliminar.izq;
                padre.der.padre = padre;
            } else { 
                padre.der = nodoAEliminar.der;
                if (padre.der != null){
                    padre.der.padre = padre;
                }                
            }

        }  _cardinal--;
    }

    // # 4: Eliminar con dos hijos
    private void eliminarConDosHijos (Nodo padre, Nodo nodoAEliminar, T elem){
        Nodo sucesor = buscarSucesor(nodoAEliminar);

        // 1. acomodo el lugar donde estaba el sucesor 
        desconectarSucesor(sucesor);

        // 2. acomodo el lugar que saqué 
        if (esHijoIzquierdo(padre, elem)){
            padre.izq = sucesor;
        } else {
            padre.der = sucesor;
        }

        // 3. el sucesor se queda con los hijos del nodo que elimino
        sucesor.der = nodoAEliminar.der;
        sucesor.izq = nodoAEliminar.izq;

        sucesor.padre = padre;

        if (sucesor.izq != null) {
            sucesor.izq.padre = sucesor;
        }
        if (sucesor.der != null) {
            sucesor.der.padre = sucesor;
        }

        _cardinal--;
    }

    // # 5: Buscar padre
    private Nodo buscarPadreRecursivo(Nodo nodo, T elem){ 
    Nodo padre = nodo;

    if (nodo == null) {
        padre = null;
    } else if ((nodo.izq != null && nodo.izq.valor.compareTo(elem) == 0) || (nodo.der != null && nodo.der.valor.compareTo(elem) == 0)){
        padre = nodo;
    // sigo buscando si no es ninguno de esos
    } else if (nodo.valor.compareTo(elem) > 0){
        padre = buscarPadreRecursivo(nodo.izq, elem);
    } else if (nodo.valor.compareTo(elem) < 0){
        padre = buscarPadreRecursivo(nodo.der, elem); 
    }
    return padre;
    }  

    // 6 #: Busco nodo del que quiero eliminar
    private Nodo buscarNodoAEliminar(Nodo padre, T elem){
        Nodo aEliminar = null;

        if (esHijoIzquierdo(padre,elem)){
            aEliminar = padre.izq;
        } else {
            aEliminar = padre.der;
        }

        return aEliminar;
    }

    // 7 #: ¿Está a la izquierda?
    private boolean esHijoIzquierdo(Nodo padre, T elem){
        return padre.izq != null && padre.izq.valor.compareTo(elem) == 0;
    }

    
    // 8 #: Busco el sucesor (el más chico del lado derecho)
    private Nodo buscarSucesor(Nodo nodo) {
        Nodo actual = nodo.der;
        while (actual != null && actual.izq != null) {
            actual = actual.izq;
        }
        return actual;
    }

    // 9 #: Cantidad de hijos de un nodo
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

    // 10 #: Acomodo caundo saco el sucesor
    private void desconectarSucesor(Nodo sucesor) {
    Nodo hijoDerSucesor = sucesor.der;
    
    if (sucesor.padre.izq == sucesor) {
        sucesor.padre.izq = hijoDerSucesor;
    } else {
        sucesor.padre.der = hijoDerSucesor;
    }

    if (hijoDerSucesor != null) {
        hijoDerSucesor.padre = sucesor.padre;
    }
    }


    public String toString(){
        String mensaje = "";
        mensaje += "{";
        mensaje = completarStringRecursivo(_raiz,mensaje);
        mensaje += "}"; 
        return mensaje;
    }

    private String completarStringRecursivo(Nodo nodo, String mensajeACompleatr){
        if (nodo == null){
            return mensajeACompleatr;
        }
        mensajeACompleatr = completarStringRecursivo(nodo.izq, mensajeACompleatr);
        if (mensajeACompleatr.length() > 1){
            mensajeACompleatr += ","; 
        }
        mensajeACompleatr += nodo.valor;

        mensajeACompleatr = completarStringRecursivo(nodo.der, mensajeACompleatr);

        return mensajeACompleatr;
    }

    public class ABB_Iterador {
        private Nodo _actual;

        public ABB_Iterador(){
            // quiero empezar desde el más chicquito
            _actual = _raiz;
            if (_actual != null){
                while (_actual.izq != null){
                    _actual = _actual.izq;
                }
            }
        }
        public boolean haySiguiente() {            
            return _actual != null;
            //throw new UnsupportedOperationException("No implementada aun");
        }
    
        public T siguiente() {
            T valor = _actual.valor;

            // cuando tengo parte de la derecha
            if (_actual.der != null){
                _actual = buscarSucesor(_actual);
            
            // cuando no. tenog que hayar el primer padre del que soy hijo izq
            } else {
                Nodo padre = _actual.padre;
                while (padre != null && _actual == padre.der){
                    _actual = padre;
                    padre = _actual.padre;
                }
                _actual = padre;
            }
            return valor;
        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }

}

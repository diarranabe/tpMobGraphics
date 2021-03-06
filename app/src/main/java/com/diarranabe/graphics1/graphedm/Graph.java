package com.diarranabe.graphics1.graphedm;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by diarranabe on 04/10/2017.
 */

public class Graph {

    public int MAX_X = 600;
    public int MAX_Y = 800;
    private List<Node> nodes = new ArrayList<Node>();/*Les nodes du graphe*/
    private List<Arc> arcs = new ArrayList<Arc>();/*Les arcs qui relient les nodes*/
    public static List<Integer> nodeColors = new ArrayList<>();/*Les couleurs possibles pour les nodes*/

    /**
     * Constructeur avec le nombre de nodes et les dimensions initiales
     *
     * @param n
     * @param screnH
     * @param screnW
     */
    public Graph(int n, int screnH, int screnW) {
        initNodeColors();
        MAX_X = screnH;
        MAX_Y = screnW;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = Node.getRandomCoord(MAX_X);
            int y = Node.getRandomCoord(MAX_Y);
            Node node = new Node(x, y);
            node = new Node(y, x);
            node.setColor(getRandomColor());
            int num = getNoeuds().size() + 1;
            node.setEtiquete("" + num);
            boolean add = addNode(node);
            while (!add) {
                x = Node.getRandomCoord(MAX_X);
                y = Node.getRandomCoord(MAX_Y);
                node = new Node(x, y);
                node.setColor(getRandomColor());
                num = getNoeuds().size() + 1;
                node.setEtiquete("" + num);
                add = addNode(node);
            }
        }
    }

    /**
     * Constructeur avec le nombre de nodes de depart
     *
     * @param n
     */
    public Graph(int n) {
        initNodeColors();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = Node.getRandomCoord(MAX_X);
            int y = Node.getRandomCoord(MAX_Y);
            Node node = new Node(x, y);
            node = new Node(y, x);
            node.setColor(getRandomColor());
            int num = getNoeuds().size() + 1;
            node.setEtiquete("" + num);
            boolean add = addNode(node);
            while (!add) {
                x = Node.getRandomCoord(MAX_X);
                y = Node.getRandomCoord(MAX_Y);
                node = new Node(x, y);
                node.setColor(getRandomColor());
                num = getNoeuds().size() + 1;
                node.setEtiquete("" + num);
                add = addNode(node);
            }
        }
    }

    /**
     * Initialise les couleurs disponibles
     */
    public void initNodeColors() {
        nodeColors.add(Color.BLUE);
        nodeColors.add(Color.CYAN);
        nodeColors.add(Color.DKGRAY);
        nodeColors.add(Color.RED);
        nodeColors.add(Color.GRAY);
        nodeColors.add(Color.GREEN);
        nodeColors.add(Color.MAGENTA);
        nodeColors.add(Color.LTGRAY);
        nodeColors.add(Color.YELLOW);
    }

    /**
     * @return ArrayList de tous les nodes
     */
    public List<Node> getNoeuds() {
        return nodes;
    }

    /**
     * @return ArrayList de tous les arcs
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    /**
     * @return La liste de toutes couleurs disponibles
     */
    public List<Integer> getNodeColors() {
        return nodeColors;
    }

    /**
     * @return Une couleur de manière aléatoire
     */
    public static int getRandomColor() {
        Random rand = new Random();
        return nodeColors.get(rand.nextInt(nodeColors.size()));
    }

    /**
     * Ajoute un nouveau Node au graphe quand c'est possible
     *
     * @param node
     * @return true quand le Node est ajouté
     */
    public boolean addNode(Node node) {
        boolean overlap = false;
        Iterator<Node> i = nodes.iterator();
        baka:
        while (i.hasNext()) {
            Node n = i.next();
            if (Node.overlap(n, node)) {
                overlap = true;
                break baka;
            }
        }
        if (!overlap) {
            this.nodes.add(node);
        }
        return !overlap; // prouve que le node à bien de l'espace
    }

    /**
     * Retourne le node selectionné ou null
     *
     * @param x
     * @param y
     * @return
     */
    public Node selectedNode(int x, int y) {
        Node node = new Node(x, y);
        boolean overlap = false;

        Iterator<Node> i = nodes.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (Node.overlap(n, node)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Ajoute un nouveau Node quand c'est possible
     *
     * @param x
     * @param y
     * @return true quand le Node est ajouté
     */
    public boolean addNode(int x, int y) {
        Node node = new Node(x, y);
        boolean overlap = false;
        Iterator<Node> i = nodes.iterator();
        baka:
        while (i.hasNext()) {
            Node n = i.next();
            if (Node.overlap(n, node)) {
                overlap = true;
                break baka;
            }
        }
        if (!overlap) {
            this.nodes.add(node);
        }
        return !overlap; // prouve que le node à bien de l'espace
    }

    /**
     * Supprime un Node du graphe en suppriment aussi tous les arcs qui lui sont reliés
     *
     * @param node
     */
    public void removeNode(Node node) {
        try {
            removeNodeArcs(node);
            nodes.remove(node);
        } catch (ConcurrentModificationException e) {
            removeNode(node); // passe 1 fois sur 4 donc executé plusieurs fois par appel
        }
    }

    /**
     * Supprimes tous les arcs dans lesquels un node est impliqué
     *
     * @param node
     */
    public void removeNodeArcs(Node node) {
        for (Arc arc : arcs) {
            if (arc.contains(node)) {
                arcs.remove(arc);
            }
        }
    }

    /**
     * Supprime le Node à la position nodeIndex du graphe en suppriment aussi tous les arcs qui lui sont reliés
     *
     * @param nodeIndex
     */
    public void removeNode(int nodeIndex) {
        if (nodeIndex < nodes.size()) {
            Node node = nodes.get(nodeIndex);
            removeNodeArcs(node);
            nodes.remove(node);
        }
    }

    /**
     * Ajoute un Arc
     *
     * @param arc
     */
    public void addArc(Arc arc) {
        if (!Node.overlap(arc.getDebut(), arc.getFin()))
            this.arcs.add(arc);
    }

    /**
     * Ajoute un arc entre les nodes index1 et index2 de la liste de nodes
     *
     * @param index1
     * @param index2
     */
    public void addArc(int index1, int index2) {
        if (index1 != index2) {
            Node n1 = getNoeuds().get(index1);
            Node n2 = getNoeuds().get(index2);
            if (!Node.overlap(n1, n2)) {
                Log.d("XXXXAD", "add arc ");
                this.arcs.add(new Arc(n1, n2));
            }
        }

    }

    /**
     * Retourne l'arc selectionné ou null
     *
     * @param x
     * @param y
     * @return arc
     */
    public Arc selectedArc(int x, int y) {
        boolean overlap = false;

        ListIterator<Arc> it = arcs.listIterator(arcs.size());
        while (it.hasPrevious()) {
            Arc arc = it.previous();
            if (arc.isSelected(x,y)) {
                return arc;
            }
        }
        return null;
    }

    /**
     * Supprime un Arc du graphe
     *
     * @param arc
     */
    public void removeArc(Arc arc) {
        arcs.remove(arc);
    }

    /**
     * Supprime l'Arc à la position arcIndex des arcs du graphe
     *
     * @param arcIndex
     */
    public void removeArc(int arcIndex) {
        arcs.remove(getArcs().get(arcIndex));
    }

    /**
     * Retoune l'index d'un Node de manière aléatoire de la liste de nodes
     *
     * @return un index
     */
    public int getRandomNodeIndex() {
        Random rand = new Random();
        int x = rand.nextInt(getNoeuds().size());
        return x;
    }

    /**
     * Ajoute n arcs de manière aléatoire (n<=nombre de nodes)
     */
    public void addRandomArcs() {
        for (int i = 0; i < nodes.size(); i++) {
            addArc(getRandomNodeIndex(), getRandomNodeIndex());
        }
    }

    /**
     * Ajoute n arcs de manière aléatoire (n<=nombre de nodes)
     *
     * @param n si n > nombre de nodes il est ignoré, addRandomArcs() est executé
     */
    public void addRandomArcs(int n) {
        if (n < nodes.size()) {
            for (int i = 0; i < n; i++) {
                addArc(getRandomNodeIndex(), getRandomNodeIndex());
            }
        } else {
            addRandomArcs();
        }
    }


    /**
     * Retourne tous les arcs dont le Node en parametre en le debut
     *
     * @param nodeBegin si nodeBegin n'est lie a aucun autre node on retourne une liste vide
     */

    public List<Arc> getArcOutOfMe(Node nodeBegin) {
        List<Arc> arcOutOfMe = new ArrayList<Arc>();
        int i = 0;
        int e = 0;
        for (Arc arc : arcs) {
            if (arc.containsbegin(nodeBegin)) {
                arcOutOfMe.add(arc);
                e++;
            }
            i++;
        }
        return arcOutOfMe;
    }


}

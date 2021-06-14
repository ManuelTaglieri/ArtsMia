package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	private double pesoMax;
	private int lun;
	private List<ArtObject> percorsoMigliore;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
		pesoMax = 0;
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungere i vertici
		//1. -> Recupero tutti gli ArtObject dal db
		//2. -> Li inserisco come vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungere gli archi
		//APPROCCIO 1
		// -> Doppio ciclo for sui vertici
		// -> Dati due vertici, controllo se sono collegati
		//Non giunge a termine -> ci sono troppi vertici!
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for (ArtObject a2 : this.grafo.vertexSet()) {
				if (!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {
					//devo collegare a1 ad a2?
					int peso = dao.getPeso(a1, a2);
					if (peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}*/
		
		//APPROCCIO 3 (meglio usare sempre questo!)
		
		for (Adiacenza a : dao.getAdiacenze()) {
			Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		System.out.println("GRAFO CREATO!");
		System.out.println("# VERTICI: " + grafo.vertexSet().size());
		System.out.println("# ARCHI: " + grafo.edgeSet().size());
		
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean presente(Integer idObj) {
		
		ArtObject corrente = this.idMap.get(idObj);
		
		if (corrente!=null) {
			return this.grafo.containsVertex(corrente);
		} else
			return false;
		
	}
	
	public int getCompConnessa(Integer id) {
		
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> ispettore = new ConnectivityInspector<>(this.grafo);
		
		Set<ArtObject> componente = null;
		if (this.grafo.containsVertex(this.idMap.get(id))) {
			componente = ispettore.connectedSetOf(this.idMap.get(id));
			return componente.size();
		} else {
			return 0;
		}
		
	}
	
	public List<ArtObject> ricerca(int lun, Integer id) {
		
		ArtObject partenza = this.idMap.get(id);
		List<ArtObject> percorso = new LinkedList<>();
		percorso.add(partenza);
		this.lun = lun;
		ricorsiva(0, partenza, percorso, 0);
		return this.percorsoMigliore;
		
	}

	private void ricorsiva(int l, ArtObject partenza, List<ArtObject> percorso, double peso) {
		
		if (l==lun) {
			if (peso>pesoMax) {
				this.pesoMax = peso;
				this.percorsoMigliore = new LinkedList<>(percorso);
			}
			return;
		} else {
			
			for (ArtObject a : Graphs.neighborListOf(this.grafo, partenza)) {
				if (partenza.getClassification().equals(a.getClassification())) {
					percorso.add(a);
					ricorsiva(l+1, a, percorso, peso+this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, a)));
					percorso.remove(a);
				}
			}
			
		}
		
	}
	
	public Graph<ArtObject, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public double getPesoMax() {
		return this.pesoMax;
	}

}

package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	Graph<Match, DefaultWeightedEdge> grafo;
	private Map <Integer, Match> idMap;
	private PremierLeagueDAO prem = new PremierLeagueDAO();
	private List <Match> partite = new LinkedList<Match>();
	private List <Integer> id = new LinkedList<Integer>();
	private List <Match> vertici = new LinkedList<Match>();
	private List<Arco> archi = new LinkedList<Arco>();
	private List<Arco> archiBest = new LinkedList<Arco>();
	private List<Match>percorsoMigliore;
 	
	public void creaGrafo(Integer mese, Double minuti) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		partite.addAll(prem.listAllMatches());
		this.idMap = new LinkedHashMap<Integer, Match>();
		for(Match m : partite) {
			if(! idMap.containsKey(m.getMatchID())) {
				idMap.put(m.getMatchID(), m);
			}
		}
		
		id.addAll(prem.listAllMatchesMese(mese));
		for(Integer i: id) {
			vertici.add(idMap.get(i));
		}
		Graphs.addAllVertices(this.grafo, vertici);
		archi.addAll(prem.getAllArchi(mese, minuti));
		for(Arco a: archi) {
			if(! this.grafo.containsEdge(idMap.get(a.getId1()), idMap.get(a.getId2()))) {
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
			}
		}
		
	}
	
	
	public int numeroVertici(){
		int size = this.grafo.vertexSet().size();
		return size;
	}

	public int numeroArchi(){
		int size = this.grafo.edgeSet().size();
		return size;
	}
	
	public List<Arco> getMostPlayed(){
		
		int max = 0;
		for(Arco a: this.archi) {
			if(a.getPeso()> max)
				max = a.getPeso();
		}
		
		for(Arco a:this.archi) {
			if(a.getPeso() == max)
				archiBest.add(a);
		}
		
		return archiBest;
	}
	
	public List<Match>getTendina(){
		return this.vertici;
	}
	
	public List<Match> camminoMigliore(Match partenza, Match arrivo){
		this.percorsoMigliore = null;
		List<Match>parziale = new ArrayList<Match>();
		parziale.add(partenza);
		cerca(parziale, arrivo);
		return this.percorsoMigliore;
	}

	private double pesoMax = 0.0;
	private void cerca(List<Match> parziale, Match arrivo) {
		
		Match ultimo = parziale.get(parziale.size()-1);
		if(ultimo.equals(arrivo)) {
			if (percorsoMigliore == null || calcolaPeso(parziale)>pesoMax) {
				this.percorsoMigliore = new ArrayList<Match>(parziale);
				this.pesoMax = calcolaPeso(parziale);
				return;
			}
		}
		
		for(Match m: Graphs.neighborListOf(this.grafo, ultimo)) {
			if(!parziale.contains(m) && this.differentTeams(ultimo, m) == false) {
				parziale.add(m);
				cerca(parziale, arrivo);
				parziale.remove(parziale.size()-1);
			}
		}
	}


	private double calcolaPeso(List<Match> parziale) {
		
		double peso = 0.0;
		for(int i=0; i<parziale.size()-1; i++) {
			Match m1 = parziale.get(i);
			Match m2 = parziale.get(i+1);
			DefaultWeightedEdge e = this.grafo.getEdge(m1, m2);
			peso += this.grafo.getEdgeWeight(e);
		}
		return peso;
	}
	
	public boolean differentTeams(Match m1, Match m2) {
		if((m1.getTeamHomeID() == m2.getTeamAwayID()) || (m1.getTeamHomeID() == m2.getTeamHomeID()) || (m1.getTeamAwayID() == m2.getTeamHomeID())|| (m1.getTeamAwayID() == m2.getTeamAwayID())) {
			return false;
		}
		
		return true;
	}
	
	public double peso() {
		return this.pesoMax;
	}
}

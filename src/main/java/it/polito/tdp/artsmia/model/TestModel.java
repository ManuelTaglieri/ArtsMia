package it.polito.tdp.artsmia.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo();
		
		int max = 0;
		
		for (int i = 0 ; i <1132; i++) {
			if (model.getCompConnessa(i)>max) {
				max = model.getCompConnessa(i);
				System.out.println(i);
			}
		}

	}

}

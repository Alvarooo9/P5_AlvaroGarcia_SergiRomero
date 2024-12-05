package backtracking;
import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucioBacktracking {

	/* TODO
	 * cal definir els atributs necessaris
	 */
	private final Encreuades repte;
	private char[][] solucioActual;
	private char[][] millorSolucio;
	private boolean[][] ocupat;
	private int millorPuntuacio;

	
	public SolucioBacktracking(Encreuades repte) {
		this.repte = repte;
		this.solucioActual= new char[repte.getPuzzle().length][repte.getPuzzle()[0].length];
		this.millorSolucio= new char[repte.getPuzzle().length][repte.getPuzzle()[0].length];
		this.ocupat = new boolean[repte.getPuzzle().length][repte.getPuzzle()[0].length];
		this.millorPuntuacio = Integer.MIN_VALUE;
	}

	public char[][] getMillorSolucio() {

		return millorSolucio;
	}

	public Runnable start(boolean optim)
	{
		/* TODO
		 * cal inicialitzar els atributs necessaris
		 */

		if(!optim) {
			if (!this.backUnaSolucio(0))
				throw new RuntimeException("solució no trobada");
			guardarMillorSolucio();
		}else
			this.backMillorSolucio(0);
		return null;
	}

	/* esquema recursiu que troba una solució
	 * utilitzem una variable booleana (que retornem)
	 * per aturar el recorregut quan haguem trobat una solució
	 */
	private boolean backUnaSolucio(int indexUbicacio) {
		boolean trobada = false;
		// iterem sobre els possibles elements
		for(int indexItem = 0; indexItem < this.repte.getItemsSize() && !trobada; indexItem++) {
			//mirem si l'element es pot posar a la ubicació actual
			if(acceptable( indexUbicacio, indexItem)) {
				//posem l'element a la solució actual
				anotarASolucio(indexUbicacio, indexItem);

				if(esSolucio(indexUbicacio)) { // és solució si totes les ubicacions estan plenes
					return true;
				} else
					trobada = this.backUnaSolucio(indexUbicacio+1); //inserim la següent paraula
				if(!trobada)
					// esborrem la paraula actual, per després posar-la a una altra ubicació
					desanotarDeSolucio(indexUbicacio, indexItem);
			}
		}
		return trobada;
	}
	/* TODO
	 * Esquema recursiu que busca totes les solucions
	 * no cal utilitzar una variable booleana per aturar perquè busquem totes les solucions
	 * cal guardar una COPIA de la millor solució a una variable
	 */
	private void backMillorSolucio(int indexUbicacio) {
		if (indexUbicacio >= repte.getEspaisDisponibles().size()) {
			int puntuacioActual = calcularFuncioObjectiu(solucioActual);
			if (puntuacioActual > millorPuntuacio) {
				millorPuntuacio = puntuacioActual;
				guardarMillorSolucio();
			}
			return;
		}
		PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);
		for (int indexItem = 0; indexItem < repte.getItemsSize(); indexItem++) {
			if (acceptable(indexUbicacio, indexItem)) {
				anotarASolucio(indexUbicacio, indexItem);
				backMillorSolucio(indexUbicacio + 1);
				desanotarDeSolucio(indexUbicacio, indexItem);
			}
		}
	}
	
	private boolean acceptable(int indexUbicacio, int indexItem) {
		//TODO
		PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);

		if (item.length != posicio.getLength()) return false;

		for (int i = 0; i < item.length; i++) {
			int fila = posicio.getInitRow() + (posicio.getDireccio() == 'V' ? i : 0);
			int columna = posicio.getInitCol() + (posicio.getDireccio() == 'H' ? i : 0);

			if (ocupat[fila][columna] && solucioActual[fila][columna] != item[i]) {
				return false;
			}
		}
		return true;
	}
	
	private void anotarASolucio(int indexUbicacio, int indexItem) {
		//TODO
		PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);

		for (int i = 0; i < item.length; i++) {
			int fila = posicio.getInitRow() + (posicio.getDireccio() == 'V' ? i : 0);
			int columna = posicio.getInitCol() + (posicio.getDireccio() == 'H' ? i : 0);

			solucioActual[fila][columna] = item[i];
			ocupat[fila][columna] = true;
		}
	}
	
	private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
		//TODO
		PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);

		for (int i = 0; i < item.length; i++) {
			int fila = posicio.getInitRow() + (posicio.getDireccio() == 'V' ? i : 0);
			int columna = posicio.getInitCol() + (posicio.getDireccio() == 'H' ? i : 0);

			solucioActual[fila][columna] = '\0';
			ocupat[fila][columna] = false;
		}
	}

	
	private boolean esSolucio(int index) {
		//TODO
		return index >= repte.getEspaisDisponibles().size();
	}
	
	private int calcularFuncioObjectiu(char[][] matriu) {
		 //TODO
		int puntuacio = 0;
		for (char[] fila : matriu) {
			for (char c : fila) {
				if (c != '\0') puntuacio++;
			}
		}
		return puntuacio;
	}
	
	private void guardarMillorSolucio() {
		// TODO - cal guardar un clone
		for (int i = 0; i < solucioActual.length; i++) {
			millorSolucio[i] = solucioActual[i].clone();
		}
	}
	
	public String toString() {
		String resultat = "";
		//TODO
		for (int i = 0; i < millorSolucio.length; i++) {
			for (int j = 0; j < millorSolucio[i].length; j++) {
				resultat += (millorSolucio[i][j] == '\0' ? '.' : millorSolucio[i][j]);
			}
			if (i < millorSolucio.length - 1) {
				resultat += "\n";
			}
		}
		return resultat;

	}

}

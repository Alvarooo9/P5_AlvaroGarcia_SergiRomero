package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucióVoraç {
    /* TODO
     * cal definir els atributs necessaris
     */

    private final Encreuades repte;
    private char[][] solucio;

    public SolucióVoraç(Encreuades repte) {
        this.repte = repte;
        // Inicialitzar la matriu solució com a còpia del puzzle inicial
        this.solucio = new char[repte.getPuzzle().length][repte.getPuzzle()[0].length];
        for (int i = 0; i < repte.getPuzzle().length; i++) {
            System.arraycopy(repte.getPuzzle()[i], 0, solucio[i], 0, repte.getPuzzle()[0].length);
        }

        // Executar l'algorisme greedy
        this.solucio = greedy();
    }

    public char[][] getSolucio() {
        return solucio;// TODO
    }

    private char[][] greedy(){
        // mireu l'esquema utilitzat per la professora Lina
        // si voleu podeu modificar la capçalera d'aquest mètode
        // si voleu podeu utilitzar recursivitat
        // TODO
        // Iterem sobre els espais disponibles
        for (PosicioInicial espai : repte.getEspaisDisponibles()) {
            int millorIndex = -1;
            int millorPuntuacio = Integer.MIN_VALUE;

            // Provar totes les paraules per trobar la millor opció
            for (int i = 0; i < repte.getItemsSize(); i++) {
                if (esCollocable(espai, i)) {
                    int puntuacio = calcularPuntuacio(espai, i);
                    if (puntuacio > millorPuntuacio) {
                        millorPuntuacio = puntuacio;
                        millorIndex = i;
                    }
                }
            }

            // Col·locar la millor paraula trobada
            if (millorIndex != -1) {
                String paraula = new String(repte.getItem(millorIndex));
                int fila = espai.getInitRow();
                int columna = espai.getInitCol();

                // Escriure la paraula en la matriu
                for (int j = 0; j < paraula.length(); j++) {
                    if (espai.getDireccio() == 'H') {
                        solucio[fila][columna + j] = paraula.charAt(j);
                    } else {
                        solucio[fila + j][columna] = paraula.charAt(j);
                    }
                }
            }
        }
        return solucio;


    }

    private boolean esCollocable(PosicioInicial espai, int indexParaula) {
        String paraula = new String(repte.getItem(indexParaula));
        int fila = espai.getInitRow();
        int columna = espai.getInitCol();

        // Verificar orientació (horitzontal o vertical) i límits
        if (espai.getDireccio() == 'H') {
            if (columna + paraula.length() > solucio[0].length) return false;
        } else {
            if (fila + paraula.length() > solucio.length) return false;
        }

        // Comprovar conflictes amb les lletres existents
        for (int i = 0; i < paraula.length(); i++) {
            char lletraActual = espai.getDireccio() == 'H' ? solucio[fila][columna + i] : solucio[fila + i][columna];
            if (lletraActual != '\0' && lletraActual != paraula.charAt(i)) return false;
        }

        return true;
    }

    private int calcularPuntuacio(PosicioInicial espai, int indexParaula) {
        String paraula = new String(repte.getItem(indexParaula));
        int puntuacio = 0;

        // La puntuació pot dependre del nombre de coincidències de lletres
        int fila = espai.getInitRow();
        int columna = espai.getInitCol();
        for (int i = 0; i < paraula.length(); i++) {
            char lletraActual = espai.getDireccio() == 'H' ? solucio[fila][columna + i] : solucio[fila + i][columna];
            if (lletraActual == paraula.charAt(i)) {
                puntuacio++;
            }
        }

        return puntuacio;
    }



    /* A aquesta classe
     * podeu definir els mètodes privats que vulgueu
     */

}

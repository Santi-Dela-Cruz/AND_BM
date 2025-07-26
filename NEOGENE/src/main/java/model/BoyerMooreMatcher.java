package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del algoritmo Boyer-Moore para búsqueda eficiente de patrones
 * en cadenas.
 * Permite obtener los pasos de comparación para animaciones o depuración.
 */
public class BoyerMooreMatcher {

    private final int ALPHABET_SIZE = 256;
    private String text;
    private String pattern;
    private int[] occ;
    private int[] f;
    private int[] s;

    /**
     * Clase que representa un paso del algoritmo Boyer-Moore.
     * Incluye la posición, si hubo coincidencia y el número de comparaciones.
     */
    public static class Step {
        public final int patternPosition;
        public final boolean match;
        public final int comparisons;

        /**
         * Constructor de un paso del algoritmo.
         * 
         * @param patternPosition Posición del patrón en el texto
         * @param match           true si hubo coincidencia
         * @param comparisons     Número de comparaciones realizadas
         */
        public Step(int patternPosition, boolean match, int comparisons) {
            this.patternPosition = patternPosition;
            this.match = match;
            this.comparisons = comparisons;
        }
    }

    /**
     * Crea un nuevo matcher para buscar un patrón en un texto.
     * 
     * @param text    Texto donde buscar
     * @param pattern Patrón a buscar
     */
    public BoyerMooreMatcher(String text, String pattern) {
        this.text = text;
        this.pattern = pattern;
        this.occ = new int[ALPHABET_SIZE];
        this.f = new int[pattern.length() + 1];
        this.s = new int[pattern.length() + 1];
        preprocess();
    }

    /**
     * Ejecuta el preprocesamiento necesario para el algoritmo Boyer-Moore.
     */
    private void preprocess() {
        initOcc();
        preprocess1();
        preprocess2();
    }

    /**
     * Inicializa la tabla de ocurrencias de caracteres para el patrón.
     */
    private void initOcc() {
        for (int i = 0; i < ALPHABET_SIZE; i++)
            occ[i] = -1;
        for (int j = 0; j < pattern.length(); j++)
            occ[pattern.charAt(j)] = j;
    }

    /**
     * Preprocesamiento para la heurística de sufijo del algoritmo.
     */
    private void preprocess1() {
        int i = pattern.length();
        int j = i + 1;
        f[i] = j;
        while (i > 0) {
            while (j <= pattern.length() && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (s[j] == 0)
                    s[j] = j - i;
                j = f[j];
            }
            i--;
            j--;
            f[i] = j;
        }
    }

    /**
     * Preprocesamiento adicional para la heurística de sufijo.
     */
    private void preprocess2() {
        int i, j = f[0];
        for (i = 0; i <= pattern.length(); i++) {
            if (s[i] == 0)
                s[i] = j;
            if (i == j)
                j = f[j];
        }
    }

    /**
     * Ejecuta la búsqueda Boyer-Moore y devuelve los pasos de comparación.
     * 
     * @return Lista de pasos (comparaciones, coincidencias, posiciones)
     */
    public List<Step> getSearchSteps() {
        List<Step> steps = new ArrayList<>();
        int m = pattern.length();
        int n = text.length();
        int i = 0;

        while (i <= n - m) {
            int j = m - 1;
            int comparisons = 0;

            while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                j--;
                comparisons++;
            }

            if (j >= 0)
                comparisons++;

            boolean matched = j < 0;
            steps.add(new Step(i, matched, comparisons));

            if (matched) {
                i += s[0];
            } else {
                i += Math.max(s[j + 1], j - occ[text.charAt(i + j)]);
            }
        }

        return steps;
    }
}
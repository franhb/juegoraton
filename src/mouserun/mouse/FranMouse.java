package mouserun.mouse;

import mouserun.game.*;
import java.util.*;

public class FranMouse extends Mouse {

    private static final int VIA_MUERTA = -1;
    private static final int NO_EXPLORADA = 0;

    private Grid posAnterior;
    private int ultimoMovimiento = 0;

    private final HashMap<Point, Integer> visitadas;

    private static int paso=0;
    public FranMouse() {
        super("Fran");
        this.visitadas = new HashMap<>();
    }

    @Override
    public int move(Grid posActual, Cheese queso) {
        Random random = new Random();
        ArrayList<Point> possibleMoves = new ArrayList<>();
        paso++;

        if (posAnterior == null) {
            System.out.printf("Paso %03d Actual (%d, %d)  \n", paso, posActual.getX(), posActual.getY());
        } else {
            System.out.printf("Actual (%d, %d), Anterior (%d, %d) \n",
                    posActual.getX(), posActual.getY(),
                    posAnterior.getX(), posAnterior.getY());
        }

        if (posActual.canGoUp()) {
            System.out.println("Puedo UP, anterior " + direccionToString(ultimoMovimiento));
            int direccion = Mouse.UP;
            int tipo = tipoSiguiente(posActual, direccion);
            if (NO_EXPLORADA == tipo) {
                return mueve(direccion, posActual);
            } else if (VIA_MUERTA != tipo) {
                possibleMoves.add(new Point(posActual, direccion, tipo));
            }

        }
        if (posActual.canGoDown()) {
            System.out.println("Puedo DOWN, anterior " + direccionToString(ultimoMovimiento));
            int direccion = Mouse.DOWN;
            int tipo = tipoSiguiente(posActual, direccion);
            if (NO_EXPLORADA == tipo) {
                return mueve(direccion, posActual);
            } else if (VIA_MUERTA != tipo) {
                possibleMoves.add(new Point(posActual, direccion, tipo));
            }
        }
        if (posActual.canGoLeft()) {
            System.out.println("Puedo LEFT, anterior " + direccionToString(ultimoMovimiento));
            int direccion = Mouse.LEFT;
            int tipo = tipoSiguiente(posActual, direccion);
            if (NO_EXPLORADA == tipo) {
                return mueve(direccion, posActual);
            } else if (VIA_MUERTA != tipo) {
                possibleMoves.add(new Point(posActual, direccion, tipo));
            }
        }
        if (posActual.canGoRight()) {
            System.out.println("Puedo RIGHT, anterior " + direccionToString(ultimoMovimiento));
            int direccion = Mouse.RIGHT;
            int tipo = tipoSiguiente(posActual, direccion);
            if (NO_EXPLORADA == tipo) {
                return mueve(direccion, posActual);
            } else if (VIA_MUERTA != tipo) {
                possibleMoves.add(new Point(posActual, direccion, tipo));
            }
        }
        //possibleMoves.add(Mouse.BOMB);

        System.out.print("Posibles: ");
        for (Point possibleMove : possibleMoves) {
            System.out.print(direccionToString(possibleMove.getDireccion()) + ", ");
        }
        System.out.println("");

        if (possibleMoves.size() == 1) {
            visitadas.put(new Point(posActual), VIA_MUERTA);
            System.out.println("** VIA_MUERTA " + new Point(posActual));
            return mueve(possibleMoves.get(0).getDireccion(), posActual);
        }

        possibleMoves.sort(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.getTipo() - p2.getTipo();
            }

        });

        System.out.print("Posibles ordenados: ");
        for (Point possibleMove : possibleMoves) {
            System.out.print(direccionToString(possibleMove.getDireccion()) + ", ");
        }
        System.out.println("");
        if (1 == 1) {
            return mueve(possibleMoves.get(0).getDireccion(), posActual);
        }

        if (!testGrid(Mouse.UP, posActual)) {
            possibleMoves.remove((Integer) Mouse.UP);
        }
        if (!testGrid(Mouse.DOWN, posActual)) {
            possibleMoves.remove((Integer) Mouse.DOWN);
        }
        if (!testGrid(Mouse.LEFT, posActual)) {
            possibleMoves.remove((Integer) Mouse.LEFT);
        }
        if (!testGrid(Mouse.RIGHT, posActual)) {
            possibleMoves.remove((Integer) Mouse.RIGHT);
        }

        ArrayList<Integer> possibleMoves1 = new ArrayList<>();
        if (possibleMoves1.isEmpty()) {
            if (posActual.canGoUp()) {
                possibleMoves1.add(Mouse.UP);
            }
            if (posActual.canGoDown()) {
                possibleMoves1.add(Mouse.DOWN);
            }
            if (posActual.canGoLeft()) {
                possibleMoves1.add(Mouse.LEFT);
            }
            if (posActual.canGoRight()) {
                possibleMoves1.add(Mouse.RIGHT);
            }
            possibleMoves1.add(Mouse.BOMB);
        }
        return mueve(possibleMoves1.get(random.nextInt(possibleMoves1.size())), posActual);

    }
    private int mueve(int direccion, Grid grid) {
        posAnterior = grid;
        Point key = new Point(posAnterior);
        if (!visitadas.containsKey(key)) {
            incExploredGrids();
            visitadas.put(key, 1);
        }
        if (visitadas.get(key) > 0) {
            visitadas.put(key, visitadas.get(key) + 1);
        }
        ultimoMovimiento = direccion;
        System.out.println("Mueve a: " + direccionToString(direccion));
        System.out.println("------------------------------------");
        return direccion;
    }

    private int tipoSiguiente(Grid posActual, int posicion) {
        //if (ultimoMovimiento == posicion) {
        Point siguiente;
        switch (posicion) {
            case Mouse.UP:
                siguiente = new Point(posActual.getX(), posActual.getY() + 1);
                break;
            case Mouse.DOWN:
                siguiente = new Point(posActual.getX(), posActual.getY() - 1);
                break;
            case Mouse.RIGHT:
                siguiente = new Point(posActual.getX() + 1, posActual.getY());
                break;
            default:
                siguiente = new Point(posActual.getX() - 1, posActual.getY());
        }

        int tipo = visitadas.getOrDefault(siguiente, NO_EXPLORADA);
        System.out.println("Tipo siguiente " + siguiente + ": " + tipo);
        return tipo;
        //}
        //return 1;
    }

    private boolean testGrid(int direction, Grid currentGrid) {
        if (posAnterior == null) {
            return true;
        }
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        switch (direction) {
            case Mouse.UP:
                y += 1;
                break;
            case Mouse.DOWN:
                y -= 1;
                break;
            case Mouse.LEFT:
                x -= 1;
                break;
            case Mouse.RIGHT:
                x += 1;
                break;
        }
        return !(posAnterior.getX() == x && posAnterior.getY() == y);
    }

    @Override
    public void newCheese() {

    }

    @Override
    public void respawned() {

    }


    private String direccionToString(int direccion) {
        switch (direccion) {
            case Mouse.UP:
                return "UP ";
            case Mouse.DOWN:
                return "DOWN ";
            case Mouse.LEFT:
                return "LEFT ";
            case Mouse.RIGHT:
                return "RIGHT ";
            case Mouse.BOMB:
                return "BOMB ";
        }
        return "---";

    }

    class MiGrid extends Grid {

        public MiGrid(int x, int y) {
            super(x, y);
        }

        @Override
        public int hashCode() {
            int hash = getX() * 1000 + getY();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MiGrid other = (MiGrid) obj;
            if (this.getX() != other.getY()) {
                return false;
            }
            return this.getX() == other.getY();
        }

        @Override
        public String toString() {
            return "(" + getX() + ", " + getY() + ")";
        }
    }

    class Point {

        private int x, y;
        private int direccion;
        private int tipo;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private Point(Grid grid) {
            this.x = grid.getX();
            this.y = grid.getY();
        }

        private Point(Grid grid, int direccion, int tipo) {
            this(grid);
            this.direccion = direccion;
            this.tipo = tipo;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getDireccion() {
            return direccion;
        }

        public void setDireccion(int direccion) {
            this.direccion = direccion;
        }

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        @Override
        public int hashCode() {
            int hash = x * 1000 + y;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Point other = (Point) obj;
            if (this.x != other.x) {
                return false;
            }
            return this.y == other.y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

    }
}

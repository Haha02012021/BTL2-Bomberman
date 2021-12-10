package entities.move_entity.enemies;

import app.BombermanGame;

import java.util.*;

public class FindPath {
    static class Point {
        int x;
        int y;
        int distanceToStart = Integer.MAX_VALUE;
        Point prevPoint = null;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean equal(Point other) {
            if (this.x == other.x && this.y == other.y) return true;
            return false;
        }

        void print() {
            if (prevPoint != null) {
                System.out.println("(" + prevPoint.x + "," + prevPoint.y + ")-(" + x + "," + y + ") : " + distanceToStart);
            }
        }
    }

    public static boolean canPassSymbols(String current, String[] symbolsCanPass) {
        for (String s: symbolsCanPass) {
            if (current.equals(s)) return true;
        }
        return false;
    }

    static List<Point> findAPath(Point s, String[] symbolsCanPass) {
        List<List<String>> map = BombermanGame.textMap;

        List<Point> list = new ArrayList<>();
        Point next = s;
        next.prevPoint = next;

        int random = (int) (Math.random() * 10) + 5;

        while (random > 0) {
            if (next != null) {
                List<Point> points = new ArrayList<>();
                int x = next.x;
                int y = next.y;
                if (canPassSymbols(map.get(x - 1).get(y), symbolsCanPass)) {
                    Point point = new Point(x - 1, y);
                    if (!point.equal(next.prevPoint)) points.add(point);
                }
                if (canPassSymbols(map.get(x + 1).get(y), symbolsCanPass)) {
                    Point point = new Point(x + 1, y);
                    if (!point.equal(next.prevPoint)) points.add(point);
                }
                if (canPassSymbols(map.get(x).get(y - 1), symbolsCanPass)) {
                    Point point = new Point(x, y - 1);
                    if (!point.equal(next.prevPoint)) points.add(point);
                }
                if (canPassSymbols(map.get(x).get(y + 1), symbolsCanPass)) {
                    Point point = new Point(x, y + 1);
                    if (!point.equal(next.prevPoint)) points.add(point);
                }

                if (points.size() != 0) {
                    int randomDirect = (int) (Math.random() * points.size()) + 0;

                    points.get(randomDirect).prevPoint = next;
                    list.add(points.get(randomDirect));

                    random--;
                    next = points.get(randomDirect);
                } else {
                    random--;
                    next = next.prevPoint;
                    list.add(next);
                }
            } else {
                break;
            }
        }

        return list;
    }

    static List<Point> findShortestPath(Point s, Point e, String[] symbolsCanPass) {
        List<List<String>> map = BombermanGame.textMap;
        int row = BombermanGame.height;
        int col = BombermanGame.width;

        List<Point> list = new ArrayList<>();
        Point[][] points = new Point[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                points[i][j] = new Point(i, j);
            }
        }

        boolean[][] visited = new boolean[row][col];
        Queue<Point> queue = new LinkedList<>();

        visited[s.x][s.y] = true;
        queue.add(s);
        s.distanceToStart = 0;

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            if (canPassSymbols(map.get(x - 1).get(y), symbolsCanPass)) {
                int newDistance = point.distanceToStart + 1;

                if (newDistance < points[x - 1][y].distanceToStart) {
                    points[x - 1][y].distanceToStart = newDistance;
                    points[x - 1][y].prevPoint = point;
                }

                if (!visited[x - 1][y]) {
                    visited[x - 1][y] = true;
                    queue.add(points[x - 1][y]);
                }

                if (points[x - 1][y].equal(e)) {
                    e = points[x - 1][y];
                    break;
                }
            }

            if (canPassSymbols(map.get(x + 1).get(y), symbolsCanPass)) {
                int newDistance = point.distanceToStart + 1;

                if (newDistance < points[x + 1][y].distanceToStart) {
                    points[x + 1][y].distanceToStart = newDistance;
                    points[x + 1][y].prevPoint = point;
                }

                if (!visited[x + 1][y]) {
                    visited[x + 1][y] = true;
                    queue.add(points[x + 1][y]);
                }

                if (points[x + 1][y].equal(e)) {
                    e = points[x + 1][y];
                    break;
                }
            }

            if (canPassSymbols(map.get(x).get(y - 1), symbolsCanPass)) {
                int newDistance = point.distanceToStart + 1;

                if (newDistance < points[x][y - 1].distanceToStart) {
                    points[x][y - 1].distanceToStart = newDistance;
                    points[x][y - 1].prevPoint = point;
                }

                if (!visited[x][y - 1]) {
                    visited[x][y - 1] = true;
                    queue.add(points[x][y - 1]);
                }

                if (points[x][y - 1].equal(e)) {
                    e = points[x][y - 1];
                    break;
                }
            }

            if (canPassSymbols(map.get(x).get(y + 1), symbolsCanPass)) {
                int newDistance = point.distanceToStart + 1;

                if (newDistance < points[x][y + 1].distanceToStart) {
                    points[x][y + 1].distanceToStart = newDistance;
                    points[x][y + 1].prevPoint = point;
                }

                if (!visited[x][y + 1]) {
                    visited[x][y + 1] = true;
                    queue.add(points[x][y + 1]);
                }

                if (points[x][y + 1].equal(e)) {
                    e = points[x][y + 1];
                    break;
                }
            }
        }

        Point point = e;

        while (!point.equal(s)) {
            list.add(point);
            if (point.prevPoint != null) point = point.prevPoint;
            else break;
        }

        Collections.reverse(list);
        return list;
    }
}

/***************************************************************************************[Solver.cc]
 The MIT License (MIT)

 Copyright (c) 2018, Sam Bayless

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 associated documentation files (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge, publish, distribute,
 sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or
 substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **************************************************************************************************/

package monosat;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {

    @Test
    public void nNodes() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 10; i++) {
            g.addNode();
            assertEquals(g.nNodes(), i + 1);
        }
    }

    @Test
    public void nEdges() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        assertEquals(g.nEdges(), 5);
    }

    @Test
    public void addNode() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 10; i++) {
            g.addNode();
            assertEquals(g.nNodes(), i + 1);
        }
    }

    @Test
    public void bitwidth() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        assertEquals(g.bitwidth(), -1);
        Graph g2 = new Graph(s, 4);
        assertEquals(g2.bitwidth(), 4);
    }

    @Test
    public void addEdge() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        assertTrue(s.solve(e_0_1, e_0_2.not(), e_1_3));
    }

    @Test
    public void addEdgeAfterSolve() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        Lit r = g.reaches(0, 3);
        assertTrue(s.solve(r));
        assertFalse(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));

        try {
            //you cannot add edges to graphs after clause learning has occured (eg, during solve calls)
            //as this may result in inconsistent solutions
            Lit bad_edge = g.addEdge(0, 1);
            fail("you cannot add edges to graphs after clause learning has occured (eg, during solve calls)");
        }catch(java.lang.Exception e){

        }
        //the solver should still be useable after this
        assertTrue(s.solve(r));
    }
    @Test
    public void addNodeAfterSolve() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        Lit r = g.reaches(0, 3);
        assertTrue(s.solve(r));
        assertFalse(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));

        try {
            //you cannot add nodes to graphs after clause learning has occured (eg, during solve calls)
            //as this may result in inconsistent solutions
            int n = g.addNode();
            fail("you cannot add nodes to graphs after clause learning has occured (eg, during solve calls)");
        }catch(java.lang.Exception e){

        }
        //the solver should still be useable after this
        assertTrue(s.solve(r));
    }
    @Test
    public void reaches() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        Lit r = g.reaches(0, 3);
        assertTrue(s.solve(r));
        assertFalse(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));

        assertFalse(s.solve(r, e_0_2.not(), e_1_3.not(), e_2_3.not()));

        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));
        //There should only be one solution to this: 0->1, 1->2, 2->3
        ArrayList<Integer> nodes = g.getPathNodes(r);
        ArrayList<Lit> edges = g.getPathEdges(r);
        assertEquals(edges.size(), 3);
        assertEquals(nodes.size(), 4);

        assertEquals(edges.get(0), e_0_1);
        assertEquals(edges.get(1), e_1_2);
        assertEquals(edges.get(2), e_2_3);
        assertTrue(e_0_1.value());
        assertTrue(e_1_2.value());
        assertTrue(e_2_3.value());
        assertFalse(e_0_2.value());
        assertFalse(e_1_3.value());

        assertTrue(s.solve(r));

    }

    @Test
    public void reachesBack() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        Lit r = g.reachesBackward(3,0);
        Lit r2 = g.reachesBackward(0,3);

        assertTrue(s.solve(r));
        assertFalse(s.solve(r2));
        assertTrue(s.solve(r));
        assertFalse(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));

        assertFalse(s.solve(r, e_0_2.not(), e_1_3.not(), e_2_3.not()));

        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));
        //There should only be one solution to this: 0->1, 1->2, 2->3
        ArrayList<Integer> nodes = g.getPathNodes(r);
        ArrayList<Lit> edges = g.getPathEdges(r);
        assertEquals(edges.size(), 3);
        assertEquals(nodes.size(), 4);

        assertEquals(edges.get(2), e_0_1);
        assertEquals(edges.get(1), e_1_2);
        assertEquals(edges.get(0), e_2_3);
        assertTrue(e_0_1.value());
        assertTrue(e_1_2.value());
        assertTrue(e_2_3.value());
        assertFalse(e_0_2.value());
        assertFalse(e_1_3.value());

        assertTrue(s.solve(r));

    }


    @Test
    public void onPath() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with one diagonal edges
        /*

        0 *--* 1
          |/ |
        2 *--* 3

         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);

        Lit r =  g.onPath(1,0,3);
        Lit r2 =  g.onPath(1,0,3);
        assertTrue(s.solve(r));
        assertTrue(r.value());
        assertTrue(r2.value());
        assertFalse(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));

        assertFalse(s.solve(r, e_0_2.not(), e_1_3.not(), e_2_3.not()));

        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));
        //There should only be one solution to this: 0->1, 1->2, 2->3
        ArrayList<Integer> nodes = g.getPathNodes(r);
        ArrayList<Lit> edges = g.getPathEdges(r);
        assertEquals(edges.size(), 3);
        assertEquals(nodes.size(), 4);

        assertEquals(edges.get(0), e_0_1);
        assertEquals(edges.get(1), e_1_2);
        assertEquals(edges.get(2), e_2_3);
        assertTrue(e_0_1.value());
        assertTrue(e_1_2.value());
        assertTrue(e_2_3.value());
        assertFalse(e_0_2.value());
        assertFalse(e_1_3.value());

        ArrayList<Integer> nodes2 = g.getPathNodes(r2);
        ArrayList<Lit> edges2 = g.getPathEdges(r2);
        assert(nodes2.size()==nodes.size());
        assertTrue(s.solve(r));

    }


    @Test
    public void maximumFlow_geq() {
        Solver s = new Solver();
        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*
  
          0 *--* 1
            |/ |
          2 *--* 3
  
           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector cmp = new BitVector(s,4);
        Lit f = g.compareMaximumFlow(0, 3,Comparison.GEQ, cmp);
        assertTrue(s.solve(f));
        assertFalse(s.solve(f, cmp.eq(3)));
        assertTrue(s.solve(f, cmp.eq(2)));
        assertTrue(s.solve(f, cmp.eq(1)));
        assertTrue(s.solve(f, e_0_1.not(), e_2_3.not()));
        assertFalse(s.solve(f, e_0_1.not(), e_2_3.not(), cmp.eq(1)));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(f));
    }

    @Test
    public void maximumFlow_gt() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector cmp = new BitVector(s,4);
        Lit f = g.compareMaximumFlow(0, 3,Comparison.GT, cmp);
        assertTrue(s.solve(f));
        assertFalse(s.solve(f, cmp.eq(3)));
        assertFalse(s.solve(f, cmp.eq(2)));
        assertTrue(s.solve(f, cmp.eq(1)));

        assertFalse(s.solve(f, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(f, e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertTrue(s.solve(f));
    }

    @Test
    public void maximumFlow_leq() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector cmp = new BitVector(s,4);
        Lit f = g.compareMaximumFlow(0, 3, Comparison.LEQ,cmp);
        assertTrue(s.solve(f));
        assertTrue(s.solve(f, cmp.eq(3)));
        assertTrue(s.solve(f, cmp.eq(2)));
        assertTrue(s.solve(f, cmp.eq(1)));

        assertFalse(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(1)));
        assertTrue(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(2)));
        assertTrue(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(3)));
        assertTrue(s.solve(f, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertTrue(s.solve(f));
    }

    @Test
    public void maximumFlow_lt() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector cmp = new BitVector(s,4);
        Lit f = g.compareMaximumFlow(0, 3, Comparison.LT, cmp);
        assertTrue(s.solve(f));
        assertTrue(s.solve(f, cmp.eq(3)));
        assertTrue(s.solve(f, cmp.eq(2)));
        assertTrue(s.solve(f, cmp.eq(1)));

        assertFalse(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(1)));
        assertFalse(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(2)));
        assertTrue(s.solve(f, e_0_1, e_0_2, e_1_3, e_2_3, cmp.eq(3)));

        assertTrue(s.solve(f, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(f, e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertTrue(s.solve(f));
    }

    @Test
    public void maximumFlow() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));

        BitVector flow = g.maximumFlow(0, 3);
        assertTrue(s.solve(flow.gt(0)));
        assertTrue(s.solve(flow.eq(0)));
        assertTrue(s.solve(flow.eq(1)));
        assertTrue(s.solve(flow.eq(2)));
        assertFalse(s.solve(flow.eq(3)));

        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, flow.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, flow.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, flow.eq(3)));

        assertTrue(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not()));


    }


    @Test
    public void distance() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));

        BitVector dist = g.distance(0, 3);
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertFalse(s.solve(dist.eq(3)));

        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not()));


    }

    @Test
    public void distanceConsts() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, 3);

        BitVector dist = g.distance(0, 3);
        assertTrue(s.solve());
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertFalse(s.solve(dist.eq(3)));

        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not()));


    }

    @Test
    public void distanceConstsGEQ() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, 3);

        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.GEQ, dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertTrue(s.solve(dist.eq(0)));
        assertTrue(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertTrue(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));

    }

    @Test
    public void distanceConstsGT() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, 3);

        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.GT, dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertTrue(s.solve(dist.eq(0)));
        assertTrue(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertTrue(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));

    }

    @Test
    public void distanceConstsLT() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, 3);

        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.LT, dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertFalse(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));

    }


    @Test
    public void distanceConstsLEQ() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, 3);

        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.LEQ, dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));

    }

    @Test
    public void distanceGEQ() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.GEQ, dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertTrue(s.solve(dist.eq(0)));
        assertTrue(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertTrue(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));


    }

    @Test
    public void distanceGT() {
        Solver s = new Solver("","/tmp/test2.gnf");

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.GT,dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertTrue(s.solve(dist.eq(0)));
        assertTrue(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertTrue(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));


    }

    @Test
    public void distanceLEQ() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.LEQ,dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertTrue(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));


    }


    @Test
    public void distanceLT() {
        Solver s = new Solver();

        Graph g = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph withone diagonal edge
          /*

          0 *--* 1
            |/ |
          2 *--* 3

           */

        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2, 2);
        Lit e_2_3 = g.addEdge(2, 3, new BitVector(s,4, 1));
        BitVector dist = new BitVector(s,4);
        Lit d = g.compareDistance(0, 3, Comparison.LT,dist);
        s.assertTrue(d);
        assertTrue(s.solve(dist.gt(0)));
        assertFalse(s.solve(dist.eq(0)));
        assertFalse(s.solve(dist.eq(1)));
        assertFalse(s.solve(dist.eq(2)));
        assertTrue(s.solve(dist.eq(3)));


        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(1)));
        assertFalse(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(2)));
        assertTrue(s.solve(e_0_1, e_0_2, e_1_3, e_2_3, dist.eq(3)));

        assertFalse(s.solve(e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(e_0_2.not(), e_1_3.not(), e_2_3.not(), dist.eq(1)));


    }


    @Test
    public void acyclicDirected() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }
        //create a directed square graph with two diagonal edges
        /*
          0 *--* 1
            |/\|
          2 *--* 3
         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);
        Lit e_3_0 = g.addEdge(3, 0);

        Lit r = g.acyclic();
        assertTrue(s.solve(r));
        assertTrue(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertFalse(s.solve(r, e_0_1, e_1_2, e_2_3, e_3_0));
        assertTrue(s.solve(r));

        assertTrue(s.solve(r.not()));
        assertFalse(s.solve(r.not(), e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r.not(), e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(r.not(), e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertTrue(s.solve(r.not(), e_0_1, e_1_2, e_2_3, e_3_0));
        assertTrue(s.solve(r.not()));
        assertTrue(s.solve(r));

    }


    @Test
    public void acyclicUndirected() {
        Solver s = new Solver();
        Graph g = new Graph(s);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }

        //create a directed square graph with two diagonal edges
        /*
          0 *--* 1
            |/\|
          2 *--* 3
         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);
        Lit e_3_0 = g.addEdge(3, 0);

        Lit r = g.acyclic(false);
        assertTrue(s.solve(r));
        assertTrue(s.solve(r, e_0_1.not(), e_2_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not()));
        assertTrue(s.solve(r, e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertFalse(s.solve(r, e_0_1, e_1_2, e_2_3, e_3_0));
        assertTrue(s.solve(r));

        assertTrue(s.solve(r.not()));
        //valid undirected cycle: e_3_0 -> e_0_2 -> e_1_2 -> e_1_3
        assertTrue(s.solve(r.not(), e_0_1.not(), e_2_3.not()));
        assertFalse(s.solve(r.not(), e_0_1.not(), e_2_3.not(), e_1_3.not()));
        assertTrue(s.solve(r.not(), e_0_2.not(), e_1_3.not()));
        assertFalse(s.solve(r.not(), e_0_2.not(), e_1_3.not(), e_2_3.not()));
        assertTrue(s.solve(r.not(), e_0_1, e_1_2, e_2_3, e_3_0));
        assertTrue(s.solve(r.not()));
        assertTrue(s.solve(r));

    }


    @Test
    public void testGraphDecisions(){
        Solver s = new Solver("-decide-theories");
        assertTrue(s.solve());
        Graph g = new Graph(s,4);
        for (int i = 0; i < 4; i++) {
            g.addNode();
        }

        //create a directed square graph with two diagonal edges
        /*
          0 *--* 1
            |/\|
          2 *--* 3
         */
        Lit e_0_1 = g.addEdge(0, 1);
        Lit e_0_2 = g.addEdge(0, 2);
        Lit e_1_3 = g.addEdge(1, 3);
        Lit e_1_2 = g.addEdge(1, 2);
        Lit e_2_3 = g.addEdge(2, 3);
        Lit e_3_0 = g.addEdge(3, 0);

        //create lots of graph predicates
        Lit r = g.acyclic();
        Lit r2 = g.acyclic(false);
        Lit r3 = g.reaches(0, 3);
        BitVector cmp = new BitVector(s,4);
        Lit f = g.compareMaximumFlow(0, 3,Comparison.GEQ, cmp);
        Lit d = g.compareDistance(0, 3, Comparison.GEQ, cmp);

        assertTrue(s.solve(r));
        assertTrue(s.solve(r, r2,r3, f,d));
        assertTrue(s.solve(monosat.Logic.or(r, r2,r3, f,d)));

    }
    @Test
    public void testEmptyGraph() {
        Solver s = new Solver("-decide-theories");
        assertTrue(s.solve());
        Graph g1 = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g1.addNode();
        }


        Lit r = g1.reaches(0, 3);
        assertFalse(s.solve(r));
        assertTrue(s.solve(r.not()));




    }

    @Test
    public void testWrongGraphLiteral() {
        Solver s = new Solver("-decide-theories");
        assertTrue(s.solve());
        Graph g1 = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g1.addNode();
        }
        Lit l = new Lit(s);
        Lit e_0_1 = g1.addEdge(0, 1);
        Lit e_0_2 = g1.addEdge(0, 2);
        Lit e_1_3 = g1.addEdge(1, 3);
        Lit e_1_2 = g1.addEdge(1, 2);
        Lit e_2_3 = g1.addEdge(2, 3);
        Lit e_3_0 = g1.addEdge(3, 0);
        Graph g2 = new Graph(s, 4);
        for (int i = 0; i < 4; i++) {
            g2.addNode();
        }

        Lit r = g1.reaches(0, 3);
        Lit m = g1.compareMaximumFlow(0, 3,Comparison.GEQ,1);
        assertTrue(s.solve(r,m));


        ArrayList<Integer> nodes1 = g1.getPathNodes(r);
        ArrayList<Lit> edges1 = g1.getPathEdges(r);
        long flow = g1.getMaxFlow(m);
        assertTrue(flow>=1);
        long f2 = g1.getEdgeFlow(m,e_0_1);
        assertTrue(f2>=1);
        long f3 = g1.getEdgeFlow(m,e_0_1,true);
        assertTrue(f3>=1);

        try{
            ArrayList<Integer> nodes2 = g1.getPathNodes(m);
            fail("you cannot read the path of a flow constraint");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Lit> edges2 = g1.getPathEdges(m);
            fail("you cannot read the path of a flow constraint");
        }catch(java.lang.Exception e){

        }

        try{
            long f4 = g1.getMaxFlow(r);
            fail("you cannot read the flow of a reach constraint");
        }catch(java.lang.Exception e){

        }
        try{
            long f4 = g1.getEdgeFlow(r,e_0_1);
            fail("you cannot read the flow of a reach constraint");
        }catch(java.lang.Exception e){

        }

        try{
            long f4 = g1.getEdgeFlow(e_0_1,m);
            fail("inverted arguments should fail");
        }catch(java.lang.Exception e){

        }

        try{
            long f4 = g1.getEdgeFlow(e_0_1,e_0_1);
            fail("you cannot read the flow of an edge by itself");
        }catch(java.lang.Exception e){

        }
        try{
            long f4 = g1.getEdgeFlow(l,e_0_1);
            fail("you cannot read the flow of a literal");
        }catch(java.lang.Exception e){

        }
        try{
            long f4 = g1.getEdgeFlow(m,l);
            fail("you cannot read the flow of a literal");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Integer> nodes2 = g2.getPathNodes(m);
            fail("you cannot read the path of a flow constraint");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Lit> edges2 = g2.getPathEdges(m);
            fail("you cannot read the path of a flow constraint");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Integer> nodes2 = g2.getPathNodes(r);
            fail("you cannot read the path of one graph from another");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Lit> edges2 = g2.getPathEdges(r);
            fail("you cannot read the path of one graph from another");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Integer> nodes2 = g1.getPathNodes(e_0_1);
            fail("you cannot read the path of an edge");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Lit> edges2 = g1.getPathEdges(e_0_1);
            fail("you cannot read the path of an edge");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Integer> nodes2 = g2.getPathNodes(e_0_1);
            fail("you cannot read the path of an edge");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Lit> edges2 = g2.getPathEdges(e_0_1);
            fail("you cannot read the path of an edge");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Integer> nodes2 = g1.getPathNodes(l);
            fail("you cannot read the path of a non-theory literal");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Lit> edges2 = g1.getPathEdges(l);
            fail("you cannot read the path of a non-theory literal");
        }catch(java.lang.Exception e){

        }
        try{
            ArrayList<Integer> nodes2 = g2.getPathNodes(l);
            fail("you cannot read the path of a non-theory literal");
        }catch(java.lang.Exception e){

        }

        try{
            ArrayList<Lit> edges2 = g2.getPathEdges(l);
            fail("you cannot read the path of a non-theory literal");
        }catch(java.lang.Exception e){

        }

    }
}
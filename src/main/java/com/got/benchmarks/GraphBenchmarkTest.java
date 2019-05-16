/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.got.benchmarks;

import com.got.app.collections.Dijkstra;
import com.got.app.collections.Graph;
import com.got.app.collections.Node;
import com.got.app.collections.Yen;
import com.got.app.helpers.DataLoader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class GraphBenchmarkTest {

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(GraphBenchmarkTest.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        public Graph graph;

        @Setup (Level.Trial)
        public void initialise() throws Exception {
            graph = loadGraph();
        }

        private Graph loadGraph() throws Exception {
            return DataLoader.load();
        }

    }

    @Benchmark
    public void a_getShortestPath(BenchmarkState state) {
        Dijkstra d =  new Dijkstra(state.graph, "distance");
        Node start = state.graph.getNodes().get(0);
        Node end = state.graph.getNodes().get(20);
        d.shortestPath(start, end, new ArrayList<>());
    }

    @Benchmark
    public void b_getShortestPathAvoidingSingleNode (BenchmarkState state) {
        Dijkstra d =  new Dijkstra(state.graph, "distance");
        Node start = state.graph.getNodes().get(2);
        Node end = state.graph.getNodes().get(23);
        List<Node> avoid = new ArrayList<>();
        avoid.add(state.graph.getNodes().get(17));
        d.shortestPath(start, end, avoid);
    }

    @Benchmark
    public void c_getShortestPathWithWayPoints (BenchmarkState state) {
        Dijkstra d =  new Dijkstra(state.graph, "distance");
        Node start = state.graph.getNodes().get(1);
        Node end = state.graph.getNodes().get(41);
        List<Node> contains = new ArrayList<>();
        contains.add(state.graph.getNodes().get(18));
        d.shortestPath(start, end, contains, new ArrayList<>());
    }

    @Benchmark
    public void d_getKShortestPaths (BenchmarkState state) {
        Yen yen = new Yen(state.graph, "distance");
        Node start = state.graph.getNodes().get(3);
        Node end = state.graph.getNodes().get(27);
        yen.kShortestPaths(start, end, 3, new ArrayList<>());
    }

    @Benchmark
    public void e_getKShortestPathsWithWayPoints (BenchmarkState state) {
        Yen yen = new Yen(state.graph, "distance");
        Node start = state.graph.getNodes().get(4);
        Node end = state.graph.getNodes().get(58);
        List<Node> contains = new ArrayList<>();
        contains.add(state.graph.getNodes().get(17));
        contains.add(state.graph.getNodes().get(21));
        yen.kShortestPaths(start, end, 3, contains, new ArrayList<>());
    }

}


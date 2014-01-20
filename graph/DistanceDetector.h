/*
 * DistanceDetector.h
 *
 *  Created on: 2014-01-08
 *      Author: sam
 */

#ifndef DISTANCEETECTOR_H_
#define DISTANCEETECTOR_H_
#include "utils/System.h"

#include "Graph.h"
#include "Reach.h"
#include "Dijkstra.h"
#include "Connectivity.h"
#include "Distance.h"
#include "core/SolverTypes.h"
#include "mtl/Map.h"
#include "WeightedDijkstra.h"

#include "utils/System.h"
#include "Detector.h"
namespace Minisat{
class GraphTheorySolver;
class DistanceDetector:public Detector{
public:
		GraphTheorySolver * outer;
		//int within;
		int source;
		double rnd_seed;
		CRef reach_marker;
		CRef non_reach_marker;
		CRef forced_reach_marker;
		Reach * positive_reach_detector;
		Reach * negative_reach_detector;
		Reach *  positive_path_detector;

		//vec<Lit>  reach_lits;
		Var first_reach_var;
		vec<int> reach_lit_map;
		vec<int> force_reason;

		struct DistLit{
			Lit l;
			int min_distance;

		};
		vec<vec<DistLit> > dist_lits;
		struct Change{
				Lit l;
				int u;
			};
		vec<Change> changed;
		vec<Var> tmp_nodes;
		vec<Change> & getChanged(){
			return changed;
		}
		WeightedDijkstra<NegativeEdgeStatus> * rnd_path;

		struct ReachStatus{
			DistanceDetector & detector;
			bool polarity;
			void setReachable(int u, bool reachable);
			bool isReachable(int u) const{
				return false;
			}

			void setMininumDistance(int u, bool reachable, int distance);


			ReachStatus(DistanceDetector & _outer, bool _polarity):detector(_outer), polarity(_polarity){}
		};
		ReachStatus *positiveReachStatus;
		ReachStatus *negativeReachStatus;

		int getNode(Var reachVar){
			assert(reachVar>=first_reach_var);
			int index = reachVar-first_reach_var;
			assert(index< reach_lit_map.size());
			assert(reach_lit_map[index]>=0);
			return reach_lit_map[index];
		}

	/*	Lit getLit(int node){

			return reach_lits[node];

		}*/
		bool propagate(vec<Assignment> & trail,vec<Lit> & conflict);
		void buildReachReason(int node,vec<Lit> & conflict);
		void buildNonReachReason(int node,vec<Lit> & conflict);
		void buildForcedEdgeReason(int reach_node, int forced_edge_id,vec<Lit> & conflict);
		void buildReason(Lit p, vec<Lit> & reason, CRef marker);
		bool checkSatisfied();
		Lit decide();
		void addLit(int from, int to, Var reach_var,int within_steps=-1);
		DistanceDetector(int _detectorID, GraphTheorySolver * _outer, DynamicGraph<PositiveEdgeStatus> &_g, DynamicGraph<NegativeEdgeStatus> &_antig, int _source, int within_steps,double seed=1);//:Detector(_detectorID),outer(_outer),within(-1),source(_source),rnd_seed(seed),positive_reach_detector(NULL),negative_reach_detector(NULL),positive_path_detector(NULL),positiveReachStatus(NULL),negativeReachStatus(NULL){}
		virtual ~DistanceDetector(){

		}
};
};
#endif /* DistanceDetector_H_ */
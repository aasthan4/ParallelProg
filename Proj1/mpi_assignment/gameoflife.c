// gameoflife.c
// Name: Ayush Asthana
// JHED: asthanaa@jhu.edu

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include "mpi.h"

#define DEFAULT_ITERATIONS 64
#define GRID_WIDTH  256
#define DIM  16     // assume a square grid
int element(int, int);

int prev(int row, int col){ if (col==0) col=DIM-1; else col=col-1; return row*DIM+col;}
int next(int row, int col){ if (col==(DIM-1)) col=0; else col=col+1; return row*DIM+col;}
int up(int row, int col) {if (row==0) row =DIM-1; else row=row-1; return row*DIM+col;}
int down(int row, int col) {if (row==(DIM-1)) row = 0; else row=row+1; return row*DIM+col;}

int topleft(int row, int col) {if (row==0) row=DIM-1; else row=row-1; if (col==0) col=DIM-1; else col=col-1; return row*DIM+col;}
int topright(int row, int col) {if (row==0) row=DIM-1; else row=row-1; if (col==(DIM-1)) col=0; else col=col+1; return row*DIM+col;}
int botleft(int row, int col) {if (row==(DIM-1)) row=0; else row=row+1; if (col==0) col=DIM-1; else col=col-1; return row*DIM+col;}
int botright(int row, int col) {if (row==(DIM-1)) row=0; else row=row+1; if (col==(DIM-1)) col=0; else col=col+1; return row*DIM+col;}

int map_new(int row, int col, int num_procs) { 
    int colw=DIM/num_procs;
    return (col/colw)*DIM*colw +(col%colw)+row*colw;
}
int main ( int argc, char** argv ) {
int row, col;

  int global_grid[ 256 ] = 
   {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
    1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
  // MPI Standard variable
  int num_procs;
  int ID, j;
  int iters = 0;
  int num_iterations;

  // Setup number of iterations
  if (argc == 1) {
    num_iterations = DEFAULT_ITERATIONS;
  }
  else if (argc == 2) {
    num_iterations = atoi(argv[1]);
  }
  else {
    printf("Usage: ./gameoflife <num_iterations>\n");
    exit(1);
  }

  // Messaging variables
  MPI_Status stat;
  // TODO add other variables as necessary


  // MPI Setup
  if ( MPI_Init( &argc, &argv ) != MPI_SUCCESS )
  {
    printf ( "MPI_Init error\n" );
  }

  MPI_Comm_size ( MPI_COMM_WORLD, &num_procs ); // Set the num_procs
  MPI_Comm_rank ( MPI_COMM_WORLD, &ID );
  
  int wid=DIM/(num_procs);
  assert ( DIM % num_procs == 0 );
  //For Gather
  int *rbuf;

  // TODO Setup your environment as necessary
  int *tmp_arr;
  int k, tmp_col=0, tmp2_col=0, nbr;

  for ( iters = 0; iters < num_iterations; iters++ ) {
    // TODO: Add Code here or a function call to you MPI code
    rbuf = (int *)malloc(DIM*DIM*sizeof(int)); 
    tmp_arr=(int *) malloc((DIM*DIM/num_procs)*sizeof(int));
    
    if (1) {
	nbr=0;
	for(row = 0; row<DIM; row++) {
	    tmp2_col=0;
	    for(col=(ID)*wid; col<((ID+1)*wid); col++) {
		if (global_grid[next(row,col)]==1) nbr++;
		if (global_grid[prev(row,col)]==1) nbr++;
		if (global_grid[up(row,col)]==1) nbr++;
		if (global_grid[down(row,col)]==1) nbr++;
		if (global_grid[topright(row,col)]==1) nbr++;
		if (global_grid[topleft(row,col)]==1) nbr++;
		if (global_grid[botright(row,col)]==1) nbr++;
		if (global_grid[botleft(row,col)]==1) nbr++;
		
		if (nbr==3) tmp_arr[row*DIM/num_procs+tmp2_col]=1;
		else if (nbr>3) tmp_arr[row*DIM/num_procs+tmp2_col]=0;
		else if (nbr<2) tmp_arr[row*DIM/num_procs+tmp2_col]=0;
		else if (nbr==2) tmp_arr[row*DIM/num_procs+tmp2_col]=global_grid[element(row, col)];
		nbr=0; 
		tmp2_col+=1;
	    }
	}
	MPI_Gather(tmp_arr, (DIM*DIM/(num_procs)), MPI_INT, rbuf, DIM*DIM/(num_procs), MPI_INT, 0, MPI_COMM_WORLD); 
	free(tmp_arr);
    }
    if (ID ==0) {
	//change to grid
	for (row=0; row<DIM; row++) {
	    for(col=0; col<DIM; col++) {
		global_grid[element(row, col)]=rbuf[map_new(row, col, num_procs)];
	    }
	}
    }
   
    free(rbuf);

	MPI_Bcast(&global_grid, 256, MPI_INT, 0, MPI_COMM_WORLD);

/*
    if (ID==0)
	MPI_Ssend(&global_grid, 256, MPI_INT, ID+1, 0, MPI_COMM_WORLD);
    if (ID==(num_procs-1))
        MPI_Recv(&global_grid, 256, MPI_INT, ID-1, 0, MPI_COMM_WORLD, &stat);
    if (ID>0 && ID<(num_procs-1)) {
	{MPI_Recv(&global_grid, 256, MPI_INT, ID-1, 0, MPI_COMM_WORLD, &stat);
	MPI_Ssend(&global_grid, 256, MPI_INT, ID+1, 0, MPI_COMM_WORLD);}
    }
*/


    // Output the updated grid state
    if ( ID == 0 ) {
      printf ( "\nIteration %d: final grid:\n", iters );
      for ( j = 0; j < GRID_WIDTH; j++ ) {
        if ( j % DIM == 0 ) {
          printf( "\n" );
        }
        printf ( "%d  ", global_grid[j] );
      }
      printf( "\n" );
    }
  }

  // TODO: Clean up memory
  MPI_Finalize(); // finalize so I can exit
}
int element(int row, int col) {
    return row*DIM+col;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <assert.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include "omp.h"

main () 
{

//  omp_set_num_threads ( 6 );

  #pragma omp parallel for dynamic blocksize=2
  for ( int i=0; i<40; i++ )
  { 
    printf("OMP Thread# %d, loop variable %d\n", omp_get_thread_num(), i);
  }
}


////////////////////////////////////////////////////////////////////////////////
//
//  class: NoSynchTime
//
//  This incorrect program is used to measure baseline performance of 
//   the task with no synchronization overhead.
//
////////////////////////////////////////////////////////////////////////////////

class NoSynchTime implements Runnable
{
  int thread_id;    // Variable containing specific id of this thread.
  
  // Create some variables for testing.
  static  int sharedvar = 0;
 
  // Run: overides Runnabale.Run, thread entry point
  public void run ()
  {
    for ( int i=0; i<10000000; i++ )
    {
      sharedvar++;
    }
  }

  // Constructor: set thread id
  NoSynchTime ( int id ) 
  {
    this.thread_id = id;
  }

  public static void main ( String[] args )
  {
    if ( 1 != args.length ) 
    {
      System.out.println ("Usage: NoSynchTime #threads");
      return;
    } 

    // Get the number of threads we are going to run from the command line
    int numthreads = Integer.parseInt ( args[0] );

    // Array to hold references to thread objects
    Thread[] threads = new Thread[numthreads];

    // create and start specified thread objects of class NoSynchTime
    for ( int i=0; i<numthreads; i++ )
    {
      threads[i] = new Thread ( new NoSynchTime(i) );
      threads[i].start();
    }

    // Await the completion of all threads
    for ( int i=0; i<numthreads; i++ )
    {
      try
      {
        threads[i].join();
      }
      catch (InterruptedException e)
      {
         System.out.println("Thread interrupted.  Exception: " + e.toString() +
                           " Message: " + e.getMessage()) ;
        return;
      }
    }
    System.out.println("Shared volatile variable = " + sharedvar);
  }
}



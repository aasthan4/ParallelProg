////////////////////////////////////////////////////////////////////////////////
//
//  class: SynchronizedBug
//
//   Simple concurrent Java program demonstrating
//      how to inccorrectly use synchronized on the this object.
//
////////////////////////////////////////////////////////////////////////////////

class SynchronizedBug implements Runnable
{
  int thread_id;    // Variable containing specific id of this thread.
  
  // Create some variables for testing.
  static volatile int sharedsynchvar = 0;
 
  // Run: overides Runnabale.Run, thread entry point
  public void run ()
  {
    for ( int i=0; i<10000000; i++ )
    {
      synchronized(this){sharedsynchvar++;}
    }
  }

  // Constructor: set thread id
  SynchronizedBug ( int id ) 
  {
    this.thread_id = id;
  }

  public static void main ( String[] args )
  {
    if ( 1 != args.length ) 
    {
      System.out.println ("Usage: SynchronizedBug #threads");
      return;
    } 

    // Get the number of threads we are going to run from the command line
    int numthreads = Integer.parseInt ( args[0] );

    // Array to hold references to thread objects
    Thread[] threads = new Thread[numthreads];

    // create and start specified thread objects of class SynchronizedBug
    for ( int i=0; i<numthreads; i++ )
    {
      threads[i] = new Thread ( new SynchronizedBug(i) );
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
    System.out.println("Shared synchronized variable = " + sharedsynchvar);
  }
}

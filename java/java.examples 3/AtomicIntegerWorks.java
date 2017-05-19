////////////////////////////////////////////////////////////////////////////////
//
//  class: AtomicIntegerWorks
//
//  Simple program exploring the limits of AtomicIntegers
//
//   Simple concurrent Java program demonstrating basic features of
//     thread creation
//     waiting on threads
//
////////////////////////////////////////////////////////////////////////////////

import java.util.concurrent.atomic.AtomicInteger;

class AtomicIntegerWorks implements Runnable
{
  int thread_id;    // Variable containing specific id of this thread.
  
  // Create some variables for testing.
  static AtomicInteger sharedatomint = new AtomicInteger ();

 
  // Run: overides Runnabale.Run, thread entry point
  public void run ()
  {
    for ( int i=0; i<10000000; i++ )
    {
      sharedatomint.incrementAndGet();
    }
  }

  // Constructor: set thread id
  AtomicIntegerWorks ( int id ) 
  {
    this.thread_id = id;
  }

  public static void main ( String[] args )
  {
    if ( 1 != args.length ) 
    {
      System.out.println ("Usage: AtomicIntegerWorks #threads");
      return;
    } 

    // Get the number of threads we are going to run from the command line
    int numthreads = Integer.parseInt ( args[0] );

    // Array to hold references to thread objects
    Thread[] threads = new Thread[numthreads];

    // create and start specified thread objects of class AtomicIntegerWorks
    for ( int i=0; i<numthreads; i++ )
    {
      threads[i] = new Thread ( new AtomicIntegerWorks(i) );
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
    System.out.println("Shared atomic integer = " + sharedatomint);
  }
}


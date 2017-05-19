////////////////////////////////////////////////////////////////////////////////
//
//  class: SimpleConc
//
//   Most basic program with multiple threads that all do different stuff.
//
////////////////////////////////////////////////////////////////////////////////

class SimpleConc implements Runnable
{
  int thread_id;    // Variable containing specific id of this thread.
  
  // Run: overides Runnabale.Run, thread entry point
  public void run ()
  {
    System.out.println ( thread_id + ": Running thread" );
  }

  // Constructor: set thread id
  SimpleConc ( int id ) 
  {
    this.thread_id = id;
  }

  public static void main ( String[] args )
  {
    if ( 1 != args.length ) 
    {
      System.out.println ("Usage: SimpleConc #threads");
      return;
    } 

    int numthreads = Integer.parseInt ( args[0] );

    // create and start specified thread objects of class SimpleConc
    for ( int i=0; i<numthreads; i++ )
    {
      new Thread ( new SimpleConc(i) ).start();
    }
  }
}



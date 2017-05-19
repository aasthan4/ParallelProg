import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
	
public class FoF {


	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException 
		{
			String line = value.toString();
			String [] split = line.split("\\s+");
			if(split.length > 0){
				String host = split[0];
                        	for(int i = 0; i < split.length; i++){
					for(int j = i+1; j < split.length; j++){
						Integer [] sorted ={Integer.parseInt(split[0]),Integer.parseInt(split[i]), Integer.parseInt(split[j])};
						Arrays.sort(sorted);

						word.set(sorted[0] + " " + sorted[1] + " " + sorted[2] + "\n");
						output.collect(word,one);
						word.set(sorted[1] + " " + sorted[0] + " " + sorted[2] + "\n");
						output.collect(word,one);
						word.set(sorted[2] + " " + sorted[0] + " " + sorted[1] + "\n");
						output.collect(word,one);
							
					
					}
				}
			}
		}
	}
	

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, NullWritable> {
		private final static NullWritable nw = NullWritable.get();
		
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, NullWritable> output, Reporter reporter) throws IOException {


			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			if(sum > 2){
				output.collect(key, nw);
			}
		}
	}
	

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(FoF.class);
		conf.setJobName("FoF");
	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(NullWritable.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(IntWritable.class);
	
		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
	
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	
		JobClient.runJob(conf);
	}
}

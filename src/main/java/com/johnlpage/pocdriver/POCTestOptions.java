package com.johnlpage.pocdriver;





import org.apache.commons.cli.CommandLine;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


//Yes - lots of public values, getters are OTT here.

public class POCTestOptions {
	int batchSize = 512;
	int numFields = 10;
	final long NUMBER_SIZE = 1000000;

	int textFieldLen = 30;

	int numThreads = 4;

	int threadIdStart = 0;

	int reportTime = 10;

	int slowThreshold = 50;

	boolean logstats = false;

	int insertops = 100;

	int opsPerSecond = 0;

	int keyqueries = 0;

	int arrayupdates = 0;

	int updates = 0;

	int rangequeries=0;

	int duration = 18000;

	int numShards = 1;

	String logfile = null;

	boolean sharded = false;

	boolean singleserver = false;

	String statsfile = "pocload.csv";

	String databaseName = "POCDB";

	String collectionName = "POCCOLL";

	String workflow = null;

	boolean emptyFirst = false;

	boolean printOnly = false;

	int secondaryidx=0;

	int arraytop = 0;

	int arraynext = 0;

	String username = null;

	char[] password = null;

	String authDatabase = null;

	int blobSize = 0;

	boolean findandmodify=false;

	int workingset = 100;

	boolean helpOnly = false;

	String connectionDetails = "mongodb://localhost:27017";

	int multistage =0;

	boolean fulltext;

	public POCTestOptions(String[] args) throws ParseException
	{
		CommandLineParser parser = new DefaultParser();
		
		Options cliopt;
		cliopt = new Options();
		cliopt.addOption("a","arrays",true,"Shape of any arrays in new sample records x:y so -a 12:60 adds an array of 12 length 60 arrays of integers");
		cliopt.addOption("b","bulksize",true,"Bulk op size (default 512)");
		cliopt.addOption("c","host",true,"Mongodb connection details (default 'mongodb://localhost:27017' )");
		cliopt.addOption("d","duration",true,"Test duration in seconds, default 18,000");
		cliopt.addOption("e","empty",false,"Remove data from collection on startup");
		cliopt.addOption("f","numfields",true,"Number of top level fields in test records (default 10)");
		cliopt.addOption("g","arrayupdates",true,"Ratio of array increment ops requires option 'a' (default 0)");
		cliopt.addOption("h","help",false,"Show Help");
		cliopt.addOption("i","inserts",true,"Ratio of insert operations (default 100)");
		cliopt.addOption("j","workingset",true,"Percentage of database to be the working set (default 100)");
		cliopt.addOption("k","keyqueries",true,"Ratio of key query operations (default 0)");
		cliopt.addOption("l","textfieldsize",true,"Length of text fields in bytes (default 30)");
		cliopt.addOption("m","findandmodify",false,"Use findandmodify instead of update and retireve record (with -u or -v only)");
		cliopt.addOption("n","namespace",true,"Namespace to use , for example myDatabase.myCollection");
		cliopt.addOption("o","logfile",true,"Output stats to  <file> ");
		cliopt.addOption("p","print",false,"Print out a sample record according to the other parameters then quit");
		cliopt.addOption("q","opsPerSecond",true,"Try to rate limit the total ops/s to the specified ammount");
		cliopt.addOption("r","rangequeries",true,"Ratio of range query operations (default 0)");
		cliopt.addOption("s","slowthreshold",true,"Slow operation threshold in ms(default 50)");
		cliopt.addOption("t","threads",true,"Number of threads (default 4)");
		cliopt.addOption("u","updates",true,"Ratio of update operations (default 0)");
		cliopt.addOption("v","workflow",true,"Specify a set of ordered operations per thread from [iukp]");
		cliopt.addOption("w","nosharding",false,"Do not shard the collection");
		cliopt.addOption("x","indexes",true,"Number of secondary indexes - does not remove existing (default 0)");
		cliopt.addOption(null,"threadIdStart",true,"Start 'workerId' for each thread. 'w' value in _id. (default 0)");
		cliopt.addOption(null,"fulltext",false,"Create fulltext index (default false)");
		cliopt.addOption(null,"binary",true,"add a binary blob of size KB");
		
		CommandLine cmd = parser.parse(cliopt, args);
		

		if(cmd.hasOption("binary"))
		{
			blobSize = Integer.parseInt(cmd.getOptionValue("binary"));
		}
		
		if(cmd.hasOption("q"))
		{
			opsPerSecond = Integer.parseInt(cmd.getOptionValue("q"));
		}
		
		if(cmd.hasOption("j"))
		{
			workingset = Integer.parseInt(cmd.getOptionValue("j"));
		}
		
		if(cmd.hasOption("v"))
		{
			workflow = cmd.getOptionValue("v");
		}	
		
		if(cmd.hasOption("n"))
		{
			String ns = cmd.getOptionValue("n");
			String[] parts = ns.split("\\.");
			if(parts.length != 2)
			{
				System.err.println("namespace format is 'DATABASE.COLLECTION' ");
				System.exit(1);
			}
			databaseName=parts[0];
			collectionName=parts[1];
		}
		
		if(cmd.hasOption("a"))
		{
			String ao = cmd.getOptionValue("a");
			String[] parts = ao.split(":");
			if(parts.length != 2)
			{
				System.err.println("array format is 'top:second'");
				System.exit(1);
			}
			arraytop = Integer.parseInt(parts[0]);
			arraynext = Integer.parseInt(parts[1]);
		}
		
		
		
		if(cmd.hasOption("e"))
		{
			emptyFirst=true;
		}
		
		
		if(cmd.hasOption("p"))
		{
			printOnly=true;
		}
		
		if(cmd.hasOption("w"))
		{
			singleserver=true;
		}
		if(cmd.hasOption("r"))
		{
			rangequeries = Integer.parseInt(cmd.getOptionValue("r"));
		}
		
		if(cmd.hasOption("d"))
		{
			duration = Integer.parseInt(cmd.getOptionValue("d"));
		}
		
		if(cmd.hasOption("g"))
		{
			arrayupdates = Integer.parseInt(cmd.getOptionValue("g"));
		}
		
		if(cmd.hasOption("u"))
		{
			updates = Integer.parseInt(cmd.getOptionValue("u"));
		}
		
		if(cmd.hasOption("i"))
		{
			insertops = Integer.parseInt(cmd.getOptionValue("i"));
		}
		
		if(cmd.hasOption("x"))
		{
			secondaryidx = Integer.parseInt(cmd.getOptionValue("x"));
		}
		
		if(cmd.hasOption("o"))
		{
			logfile = cmd.getOptionValue("o");
		}
		
		if(cmd.hasOption("k"))
		{
			keyqueries = Integer.parseInt(cmd.getOptionValue("k"));
		}
		
		if(cmd.hasOption("b"))
		{
			batchSize = Integer.parseInt(cmd.getOptionValue("b"));
		}
		
		if(cmd.hasOption("s"))
		{
			slowThreshold = Integer.parseInt(cmd.getOptionValue("s"));
		}
		
		// automatically generate the help statement
		if(cmd.hasOption("h"))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "POCDriver", cliopt );
			helpOnly = true;
		}
		
		if(cmd.hasOption("c"))
		{
			connectionDetails = cmd.getOptionValue("c");
		}	
			
		if(cmd.hasOption("l"))
		{
			textFieldLen = Integer.parseInt(cmd.getOptionValue("l"));
		}
		
		if(cmd.hasOption("m"))
		{
			findandmodify = true;
		}
		
		if(cmd.hasOption("f"))
		{
			numFields = Integer.parseInt(cmd.getOptionValue("f"));
		}

		if(cmd.hasOption("o"))
		{
			statsfile = cmd.getOptionValue("o");
			logstats = true;
		}
		
		if(cmd.hasOption("t"))
		{
			numThreads = Integer.parseInt(cmd.getOptionValue("t"));
		}
		if(cmd.hasOption("fulltext"))
        {
            fulltext = true;
        }
		
		if(cmd.hasOption("threadIdStart"))
		{
			threadIdStart = Integer.parseInt(cmd.getOptionValue("threadIdStart"));
		}
		
	}

	public int getArraynext() {
		return arraynext;
	}

	public int getArraytop() {
		return arraytop;
	}

	public int getArrayupdates() {
		return arrayupdates;
	}

	public String getAuthDatabase() {
		return authDatabase;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public int getBlobSize() {
		return blobSize;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public String getConnectionDetails() {
		return connectionDetails;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public int getDuration() {
		return duration;
	}

	public int getInsertops() {
		return insertops;
	}

	public int getKeyqueries() {
		return keyqueries;
	}

	public String getLogfile() {
		return logfile;
	}

	public int getMultistage() {
		return multistage;
	}

	public long getNUMBER_SIZE() {
		return NUMBER_SIZE;
	}

	public int getNumFields() {
		return numFields;
	}

	public int getNumShards() {
		return numShards;
	}

	public int getNumThreads() {
		return numThreads;
	}

	public int getOpsPerSecond() {
		return opsPerSecond;
	}

	public char[] getPassword() {
		return password;
	}

	public int getRangequeries() {
		return rangequeries;
	}

	public int getReportTime() {
		return reportTime;
	}

	public int getSecondaryidx() {
		return secondaryidx;
	}

	public int getSlowThreshold() {
		return slowThreshold;
	}

	public String getStatsfile() {
		return statsfile;
	}

	public int getTextFieldLen() {
		return textFieldLen;
	}

	public int getThreadIdStart() {
		return threadIdStart;
	}

	public int getUpdates() {
		return updates;
	}

	public String getUsername() {
		return username;
	}

	public String getWorkflow() {
		return workflow;
	}

	public int getWorkingset() {
		return workingset;
	}

	public boolean isEmptyFirst() {
		return emptyFirst;
	}

	public boolean isFindandmodify() {
		return findandmodify;
	}

	public boolean isFulltext() {
		return fulltext;
	}

	public boolean isHelpOnly() {
		return helpOnly;
	}

	public boolean isLogstats() {
		return logstats;
	}

	public boolean isPrintOnly() {
		return printOnly;
	}

	public boolean isSharded() {
		return sharded;
	}

	public boolean isSingleserver() {
		return singleserver;
	}

	public void setArraynext(int arraynext) {
		this.arraynext = arraynext;
	}
	public void setArraytop(int arraytop) {
		this.arraytop = arraytop;
	}
	public void setArrayupdates(int arrayupdates) {
		this.arrayupdates = arrayupdates;
	}
	public void setAuthDatabase(String authDatabase) {
		this.authDatabase = authDatabase;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public void setBlobSize(int blobSize) {
		this.blobSize = blobSize;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public void setConnectionDetails(String connectionDetails) {
		this.connectionDetails = connectionDetails;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public void setEmptyFirst(boolean emptyFirst) {
		this.emptyFirst = emptyFirst;
	}
	public void setFindandmodify(boolean findandmodify) {
		this.findandmodify = findandmodify;
	}
	public void setFulltext(boolean fulltext) {
		this.fulltext = fulltext;
	}
	public void setHelpOnly(boolean helpOnly) {
		this.helpOnly = helpOnly;
	}
	public void setInsertops(int insertops) {
		this.insertops = insertops;
	}
	public void setKeyqueries(int keyqueries) {
		this.keyqueries = keyqueries;
	}
	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}
	public void setLogstats(boolean logstats) {
		this.logstats = logstats;
	}
	public void setMultistage(int multistage) {
		this.multistage = multistage;
	}
	public void setNumFields(int numFields) {
		this.numFields = numFields;
	}
	public void setNumShards(int numShards) {
		this.numShards = numShards;
	}
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	public void setOpsPerSecond(int opsPerSecond) {
		this.opsPerSecond = opsPerSecond;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public void setPrintOnly(boolean printOnly) {
		this.printOnly = printOnly;
	}
	public void setRangequeries(int rangequeries) {
		this.rangequeries = rangequeries;
	}
	public void setReportTime(int reportTime) {
		this.reportTime = reportTime;
	}
    public void setSecondaryidx(int secondaryidx) {
		this.secondaryidx = secondaryidx;
	}
    public void setSharded(boolean sharded) {
		this.sharded = sharded;
	}
    public void setSingleserver(boolean singleserver) {
		this.singleserver = singleserver;
	}
    public void setSlowThreshold(int slowThreshold) {
		this.slowThreshold = slowThreshold;
	}

	public void setStatsfile(String statsfile) {
		this.statsfile = statsfile;
	}
	public void setTextFieldLen(int textFieldLen) {
		this.textFieldLen = textFieldLen;
	}
	public void setThreadIdStart(int threadIdStart) {
		this.threadIdStart = threadIdStart;
	}
	public void setUpdates(int updates) {
		this.updates = updates;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	
	public void setWorkingset(int workingset) {
		this.workingset = workingset;
	}
}

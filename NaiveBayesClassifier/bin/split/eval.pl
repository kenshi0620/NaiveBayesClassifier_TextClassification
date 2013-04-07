#!/usr/local/bin/perl

use English;
use Carp;
use Getopt::Long;

sub Usage{
  my $message = shift;

  print STDERR $message, "\n" if $message;
  print STDERR "\nUsage: $0 < resultfile\n";

  print STDERR <<'EOM';
          resultfile must have a format like the following, 
          where "L" for "liberal" and "C" for "conservative".

          blog_file_name_1 L
          blog_file_name_2 C
          blog_file_name_3 L
	      ...
          blog_file_name_N L

EOM

    exit(1);

}

if (! &GetOptions("help", "type=s") or
    $opt_help) {
  &Usage();
}



$n=0;
$nlib=0;
$ncon_lib=0;
$nlib_con=0;
$lambda=1;
print "2";
while (<stdin>) {
print "1";
    ($id,$tag)=split;
    if ($id=~/^lib/) {
	$nlib++;
	if ($tag ne "L") {
	    $nlib_con++;
	} 
    } else {
	if ($tag ne "C") {
	    $ncon_lib++;
	}
    }
    $n++;
}

print "Total number of blogs = $n\n";
print "Total number of conservative blogs = ", $n-$nlib,"\n";
print "Total number of liberal blogs = ", $nlib,"\n";
print "Number of mistakes (conservative as liberal) = $ncon_lib\n";
print "Number of mistakes (liberal as conservative) = $nlib_con\n";
print "** Classification accuracy = ", ($n-$ncon_lib-$nlib_con)/$n, "\n";
print "** Liberal precision = ", ($nlib-$nlib_con)/($ncon_lib+$nlib-$nlib_con), "\n";
print "** Liberal recall = ", ($nlib-$nlib_con)/$nlib, "\n";
$tcr1 = $nlib/($ncon_lib + $nlib_con);
$tcr9 = $nlib/(9*$ncon_lib + $nlib_con);
$tcr999 = $nlib/(999*$ncon_lib + $nlib_con);
print "** TCR(1)=$tcr1 TCR(9)=$tcr9 TCR(999)=$tcr999\n";

	

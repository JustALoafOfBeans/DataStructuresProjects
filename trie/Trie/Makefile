
# java compiler
JAVAC = javac

# java runtime
JAVA = java

# source directory, where all the java files are located
SRCDIR = src

# output directory, where all the class files will be 
OUTDIR = bin

# compilation flags
JFLAGS = -d $(OUTDIR)

RM = rm

all:
	$(JAVAC) $(JFLAGS) $(SRCDIR)/trie/*.java

run:
	$(JAVA) -cp $(OUTDIR)/trie TrieApp

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) $(OUTDIR)/trie/*.class

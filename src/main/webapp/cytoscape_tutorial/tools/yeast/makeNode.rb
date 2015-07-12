require 'json'

inputPath = ARGV[0]
functionPath = ARGV[1]
outputPath = ARGV[2]

input = File.open(inputPath, "r")
functionFile = File.open(functionPath, "r")
output = File.open(outputPath, "w")

result = []
proteins = []
functionList = {}

while (line = functionFile.gets)
  gene, function = line.split(",", 2)
  function.delete!("\n")
  function.delete!("\"")
  functionList[gene] = function
end

output.puts "var node = "

while (line = input.gets)
  gene, target, weight = line.split("\t")
  if (!proteins.include?(gene))
    proteins << gene

    functions = functionList[gene]
    if (functions.nil?)
      fs = "unknown" 
    elsif
      fs = functions.split("; ")
    end

    data = {:data => {:id => gene, :functions => fs}}
    result << data
  end


  if (!proteins.include?(target))
    proteins << target

    functions = functionList[target]
    if (functions.nil?)
      fs = "unknown" 
    elsif
      fs = functions.split("; ")
    end

    data = {:data => {:id => target, :functions => fs}}
    result << data

  end

end

output.puts result.to_json

input.close
output.close

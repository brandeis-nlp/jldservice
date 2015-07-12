require 'json'

outputPath = ARGV[0]

output = File.open(outputPath, "w")

result = []

output.puts "var node = "

for i in 1..100
  data = {:data => {:id => "G"+i.to_s}}
  result << data
end

output.puts result.to_json

output.close

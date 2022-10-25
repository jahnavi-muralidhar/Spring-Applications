package com.example.batchsample.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.example.batchsample.entity.User;
import com.example.batchsample.repository.UserRepository;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	  @Autowired
	  public JobBuilderFactory jobBuilderFactory;

	  @Autowired
	  public StepBuilderFactory stepBuilderFactory;
	  
	  @Autowired
	  private UserRepository userRepository;
	  
	  @Bean
	  public FlatFileItemReader<User> reader() {
		  System.out.println("Item Reader start");
		  FlatFileItemReader<User> itemReader = new FlatFileItemReader<>();
	        itemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
	        itemReader.setName("csvReader");
	        itemReader.setLinesToSkip(1);
	        itemReader.setLineMapper(lineMapper());
	        System.out.println("Item Reader end");
	        return itemReader;
	  }
	  
	  private LineMapper<User> lineMapper() {
	        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

	        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setDelimiter(",");
	        lineTokenizer.setStrict(false);
	        lineTokenizer.setNames("firstName", "lastName");

	        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	        fieldSetMapper.setTargetType(User.class);

	        lineMapper.setLineTokenizer(lineTokenizer);
	        lineMapper.setFieldSetMapper(fieldSetMapper);
	        return lineMapper;

	    }
	  
	    @Bean
		public UserItemProcessor processor() {
	    	System.out.println("Item processor ----");
			return new UserItemProcessor();
		}

//		@Bean
//		public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
//			return new JdbcBatchItemWriterBuilder<User>()
//				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//				.sql("INSERT INTO people (FirstName, LastName) VALUES (:firstName, :lastName)")
//				.dataSource(dataSource)
//				.build();
//		}	
	    
	    @Bean
	    public RepositoryItemWriter<User> writer() {
	    	System.out.println("Item Writer start");
	        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
	        writer.setRepository(userRepository);
	        writer.setMethodName("save");
	        System.out.println("Item Writer End");
	        return writer;
	    }
	  
	  @Bean
	  public Job job() {
	    return jobBuilderFactory.get("importUsers")
	      //.incrementer(new RunIdIncrementer())
	      .flow(step1())
	      .end()
	      .build();
	  }

	  @Bean
	  public Step step1() {
	    return stepBuilderFactory.get("step1")
	      .<User, User> chunk(10)
	      .reader(reader())
	      .processor(processor())
	      .writer(writer())
	      .taskExecutor(taskExecutor())
	      .build();
	  }
	  
	  @Bean
	    public TaskExecutor taskExecutor() {
	        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
	        asyncTaskExecutor.setConcurrencyLimit(10);
	        return asyncTaskExecutor;
	    }
	  
	  

}

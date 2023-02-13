package binance.spot.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeInfoEntry
{
	private String symbol;
	private String status;
	private String baseAsset;
	private String quoteAsset;
	private Long baseAssetPrecision;
	private Long quotePrecision;
	private List<String> orderTypes;
	private List<Map<String, String>> filters;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getBaseAsset()
	{
		return baseAsset;
	}

	public void setBaseAsset(String baseAsset)
	{
		this.baseAsset = baseAsset;
	}

	public String getQuoteAsset()
	{
		return quoteAsset;
	}

	public void setQuoteAsset(String quoteAsset)
	{
		this.quoteAsset = quoteAsset;
	}

	public Long getBaseAssetPrecision()
	{
		return baseAssetPrecision;
	}

	public void setBaseAssetPrecision(Long baseAssetPrecision)
	{
		this.baseAssetPrecision = baseAssetPrecision;
	}

	public Long getQuotePrecision()
	{
		return quotePrecision;
	}

	public void setQuotePrecision(Long quotePrecision)
	{
		this.quotePrecision = quotePrecision;
	}

	public List<String> getOrderTypes()
	{
		return orderTypes;
	}

	public void setOrderTypes(List<String> orderTypes)
	{
		this.orderTypes = orderTypes;
	}

	public List<Map<String, String>> getFilters()
	{
		return filters;
	}

	public void setFilters(List<Map<String, String>> filters)
	{
		this.filters = filters;
	}

}

package binance.futures.model.user;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountUpdate
{
	@JsonProperty("m")
	private String reasonType;

	@JsonProperty("B")
	private List<BalanceUpdate> balances;

	@JsonProperty("P")
	private List<PositionUpdate> positions;

	public String getReasonType()
	{
		return reasonType;
	}

	public void setReasonType(String reasonType)
	{
		this.reasonType = reasonType;
	}

	public List<BalanceUpdate> getBalances()
	{
		return balances;
	}

	public void setBalances(List<BalanceUpdate> balances)
	{
		this.balances = balances;
	}

	public List<PositionUpdate> getPositions()
	{
		return positions;
	}

	public void setPositions(List<PositionUpdate> positions)
	{
		this.positions = positions;
	}

	@Override
	public String toString()
	{
		String printBlances = balances.stream().map(Object::toString).collect(Collectors.joining("\n"));
		String printPositions = positions.stream().map(Object::toString).collect(Collectors.joining("\n"));
		return "AccountUpdate [reasonType=" + reasonType + "]\nbalances\n" + printBlances + "\npositions\n" + printPositions;
	}

}

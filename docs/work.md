## [Unreleased]

### Changed

- The External Storage now supports multiple resource types on a single connected inventory.

### Fixed

- Fixed cables broken with a Wrench not stacking with newly crafted cables.
- Fixed slow performance in the Grid when searching.
- Fixed storages failing to load after removing an addon that adds more storage types.
- Fixed crash when trying to open block in spectator mode.
- Fixed crash when trying to open Grid with EnderIO autocrafting pattern in network.
- Fixed External Storage making resources flicker when the connected inventory is causing neighbor updates.

## [2.0.0-milestone.4.14] - 2025-02-23

### Added

- Autocrafting Upgrade

### Changed

- The filter slots for the Exporter, Constructor and Interface now display whether a resource is missing, the destination does not accept it, the resource cannot be autocrafted due to missing resources, or whether the resource is currently autocrafting.
- 'B' is now displayed after fluid amounts, indicating the amount in buckets.
- The Autocrafter now only connects to other autocrafters through its front face. The reason for this is so that you can connect it to an Interface and accept multi-type autocrafting inputs without the Interface being connected to your network.
- When using Autocrafter chaining, you can now only configure the locking mode on the head of the chain.
- For now, it is not possible to rotate the Relay. This behavior will be restored later.

### Fixed

- Fixed cables broken with a Wrench not stacking with any other cables.
- Fixed colored importers, exporters, and external storages not dropping when broken.
- Fixed fluid amounts displaying to 0.1 buckets and not 0.001. The suffix "m" is used.
- Fixed Pattern Grid processing matrix input slots not mentioning "SHIFT click to clear".
- Fixed amount buttons not working in the Autocrafting Preview when the amount is "0" after clicking the "Max" button.
- Fixed not being able to insert resources in a Grid when clicking on something that is autocraftable.
- Fixed not being able to specify amount &lt; 1 in filter and pattern creation slots.
- Fixed visual bug where fluid containers were being filled when hovering over a fluid in the Grid.
- Fixed clear button in the Pattern Grid for the smithing table not working.
- Fixed item count rendering behind the item in the Interface slots.
- Fixed pickaxes not influencing break speed of blocks.
- Fixed non-autocraftables Grid view filter still showing autocraftable resources as they get inserted.
- Fixed filter items from the Pattern Grid crafting matrix being dropped when the Pattern Grid is broken.
- Fixed created pattern not being insertable in an Autocrafter when it is dropped after breaking the Pattern Grid.
- Fixed autocrafting tasks being completed when not all processing outputs have been received yet.
- Fixed items not stacking when externally interacting with an Interface.
- Fixed Constructor in default scheduling mode not trying other filters when resource is not available.
- Fixed autocrafter chaining returning "Machine not found" when starting autocrafting.
- Fixed Regulator Upgrade not being allowed 4 times in the Importer or Exporter.

## [2.0.0-milestone.4.13] - 2025-02-01

### Added

- Colored variants are now moved to a separate creative mode tab.
- You can now start autocrafting tasks in the Relay's output network, using patterns and autocrafters from the input network.

### Fixed

- Fixed Crafter and Security Manager bottom sides being lit on NeoForge.
- Fixed pattern input slot in the Pattern Grid not being accessible as an external inventory.
- Fixed duplication bug with the Crafting Grid matrix and insert-only storages.
- Fixed not being able to deselect the Grid search box.
- Fixed Storage Block not showing the amount and capacity when inactive.

## [2.0.0-milestone.4.12] - 2025-01-27

### Added

- Autocrafting engine.
- The crafting preview now has the ability to fill out the maximum amount of a resource you can currently craft.
- In the crafting preview, you can now indicate whether you want to be notified when an autocrafting task is finished.

### Changed

- Autocrafting now handles multiple patterns with the same output correctly by trying to use the pattern with the
  highest priority first. If there are missing resources, lower priority patterns are checked.
- The Autocrafter now faces the block you're clicking when placing it, like the other cable blocks (like the Exporter or Importer).
- You can no longer cancel autocrafting tasks if there is not enough space in storage to return the intermediate task storage.
- The Autocrafting Monitor now shows the machine in which a resource is processing.

### Fixed

- Fixed amount in amount screens resetting when resizing the screen.

## [2.0.0-milestone.4.11] - 2024-12-08

### Added

- Ability to differentiate between insert and extract storage priorities. By default, the extract priority will match the insert priority unless configured otherwise.

### Fixed

- Fixed External Storage not connecting properly to fluid storages.
- Fixed Interface filter not respecting maximum stack size of a resource.
- Fixed potential crash when trying to build cable shapes.
- Fixed storage disk upgrade recipes not showing properly in recipe viewers.
- Protect against crashes from other mods when trying to build the cached Grid tooltip.
- Fixed charging energy items not working on Fabric.

## [2.0.0-milestone.4.10] - 2024-11-24

### Added

- Autocrafting Monitor
- Wireless Autocrafting Monitor
- Creative Wireless Autocrafting Monitor

### Changed

- The Autocrafting Monitor now has a sidebar with all tasks instead of using tabs.
- The auto-selected search box mode is now a global option used in the Autocrafter Manager as well.

### Removed

- Block of Quartz Enriched Iron (has been moved to addon mod)
- Block of Quartz Enriched Copper (has been moved to addon mod)

## [2.0.0-milestone.4.9] - 2024-11-01

### Added

- Autocrafter Manager
- You can now configure the view type of the Autocrafter Manager:
  - Visible (only show autocrafters that are configured to be visible to the Autocrafter Manager)
  - Not full (only show autocrafters that are not full yet)
  - All (show all autocrafters)

### Changed

- The search field in the Autocrafter Manager can now search in:
  - Pattern inputs
  - Pattern outputs
  - Autocrafter names
  - All of the above (by default)
- Due to technical limitations and the new filtering options listed above being client-side only, you can no longer shift-click patterns in the Autocrafter Manager.
- In the Autocrafter, you can now configure whether it is visible to the Autocrafter Manager (by default it's visible).

## [2.0.0-milestone.4.8] - 2024-10-12

### Added

- Autocrafter
  - Note: autocrafting itself hasn't been implemented yet. This is the in-game content, but not the autocrafting engine itself yet.
- The Relay now has support for propagating autocrafting when not in pass-through mode.

### Changed

- The Crafter has been renamed to "Autocrafter".
- Optimized memory usage and startup time of cable models. After updating, cables will appear disconnected, but this is only visual. Cause a block update to fix this.
- Optimized performance of searching in the Grid.
- Custom titles that overflow will now have a marquee effect instead, for every GUI.
- You can now define a priority in the Autocrafter.
- You can now change the name of a Autocrafter in the GUI.
- Changed "Crafter mode" to "Locking mode" with following options:
  - Never
  - Lock until redstone pulse is received
  - Lock until connected machine is empty (new, facilitates easier "blocking mode" without redstone)
  - Lock until all outputs are received (new, facilitates easier "blocking mode" without redstone)
  - Lock until low redstone signal
  - Lock until high redstone signal
- Resources in the Grid that are autocraftable now display an orange backdrop and tooltip to indicate whether the resource is autocraftable at a glance.
- Slots used in the Pattern Grid for pattern encoding and Crafting Grid crafting matrix slots now display an orange backdrop and tooltip to indicate whether the item is autocraftable at a glance. This checks patterns from your network and from your inventory.
- Added help tooltip for filtering based on recipe items in the Crafting Grid.
- The crafting amount and crafting preview screens have been merged. Changing the amount will update the live preview.
- The numbers on the crafting preview screen are now compacted with units.
- When requesting autocrafting multiple resources at once, which can happen via a recipe mod, all the crafting requests are now listed on the side of the GUI.
- You can now request autocrafting from the Storage Monitor if the resource count reaches zero.

### Fixed

- Fixed mouse keybindings not working on NeoForge.
- Fixed upgrade destinations not being shown on upgrades.
- Fixed resources with changed data format or ID causing entire storage to fail to load.
- Fixed crash when trying to export fluids from an External Storage on Fabric.
- The Configuration Card can now also transfer the (configured) Regulator Upgrade.

## [2.0.0-milestone.4.7] - 2024-08-11

### Added

- You can now upgrade Storage Disks and Storage Blocks to a higher tier by combining with a higher tier Storage Part. The original Storage Part will be returned.

### Changed

- Updated to Minecraft 1.21.1.
- The Network Transmitter and Wireless Transmitter GUI now has an inactive and active GUI animation.
- The Wireless Transmitter now shows whether it's inactive in GUI instead of always showing the range.

### Fixed

- Use new slimeballs convention tag for Processor Binding.
- Portable Grid search bar texture being positioned in the wrong way.
- External Storage screen unnecessarily showing upgrade slots.
- Grid setting changes not persisting after restarting Minecraft.
- Fixed not being able to extract fluids from the Grid with an empty bucket or other empty fluid container.
- All blocks and items now correctly retain their custom name.

## [2.0.0-milestone.4.6] - 2024-08-08

### Added

- Pattern Grid
- Pattern

### Changed

- The Pattern now shows the recipe in the tooltip.
- When a Pattern is created for a recipe, the Pattern will have a different texture and name to differentiate between empty patterns.
- The Pattern Grid now has additional support for encoding stonecutter and smithing table recipes.
- The Pattern output is now always rendered in the Pattern Grid result slot.
- You can now search in the Pattern Grid alternatives screen.
- In the Pattern Grid alternatives screen, all resources belonging to a tag or no longer shown at once. You can expand or collapse them.
- The tag names in the Pattern Grid alternatives screen will now be translated.
- "Exact mode" in the Pattern Grid has been replaced with "Fuzzy mode" (inverse).

### Fixed

- Clicking on a scrollbar no longer makes a clicking sound.
- Incorrect and outdated (mentioning NBT tags) help explanations for fuzzy mode.
- Amount screen allowing more than the maximum for fluids.
- Potential text overflow in the Grid for localization with long "Grid" text.

## [2.0.0-milestone.4.5] - 2024-07-26

### Added

- Ability to extract fluids from the Interface using an empty bucket or other empty fluid container.
- Support for the NeoForge config screen.

### Fixed

- Fixed crash when trying to export fluids into an Interface on Fabric.
- Fixed Relay configuration not being correct on NeoForge.
- Fixed crash in logs when trying to quick craft an empty result slot in the Crafting Grid.
- Fixed recipes not using silicon tag and Refined Storage silicon not being tagged properly.

## [2.0.0-milestone.4.4] - 2024-07-10

## [2.0.0-milestone.4.3] - 2024-07-06

### Added

- Ability to open Portable Grid with a keybinding.

### Fixed

- Fixed Relay model not being able to load correctly.
- Fixed not being able to ghost drag resources from recipe viewers into filter slots on NeoForge.
- Fixed extra dark backgrounds due to drawing background on GUIs twice.
- Fixed Configuration Card not being able to transfer upgrades for the Wireless Transmitter.
- Fixed upgrade inventories not maintaining order after reloading. Upgrade inventories from the milestone 4.2 are
  incompatible and will be empty.
- Fixed Wireless Transmitter not dropping upgrades when breaking block.

## [2.0.0-milestone.4.2] - 2024-07-06

## [2.0.0-milestone.4.1] - 2024-07-05

### Fixed

- Fixed creative mode tab icon on NeoForge showing a durability bar.

## [2.0.0-milestone.4.0] - 2024-07-04

### Added

- Ported to Minecraft 1.21.
- More help information for items.
- Quartz Enriched Copper, used to craft cables.
- Block of Quartz Enriched Copper

### Changed

- The mod ID has been changed from "refinedstorage2" to "refinedstorage". Worlds that used milestone 3 on Minecraft
  1.20.4 are no longer compatible.
- Recipes now use common tag conventions from NeoForge and Fabric.

### Fixed

- Regulator Upgrade having wrong GUI title.
- Crafting Grid not dropping crafting matrix contents when broken.
- "+1" button on amount screen not doing anything.
